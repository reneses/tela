package io.reneses.tela.core.dispatcher;

import io.reneses.tela.core.dispatcher.exceptions.ActionNotDefinedException;
import io.reneses.tela.core.dispatcher.exceptions.InvalidParameterTypeException;
import io.reneses.tela.core.dispatcher.exceptions.ModuleNotDefinedException;
import io.reneses.tela.core.dispatcher.scanner.ActionScannerFactory;
import io.reneses.tela.core.dispatcher.models.Action;
import io.reneses.tela.core.dispatcher.models.ActionHelp;
import io.reneses.tela.core.dispatcher.models.Module;
import io.reneses.tela.core.sessions.models.Session;
import io.reneses.tela.core.sessions.exceptions.ModuleTokenNotFoundException;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Dispatcher for the actions
 */
class ActionDispatcherImpl extends AbstractActionDispatcher {

    private Map<String, Module> modules = new HashMap<>();


    //----------------------- Constructor and singleton getter -----------------------//

    /**
     * Create an action dispatcher, scanning classes within the package where it is created
     *
     * @param actionClasses  Classes to be scanned
     * @param actionPackages Packages to be scanned
     */
    ActionDispatcherImpl(Collection<Class<?>> actionClasses, Collection<Package> actionPackages) {
        super(actionClasses, actionPackages);
        this.modules = ActionScannerFactory.create(actionClasses, actionPackages).scan();
    }


    //----------------------- Facade for the function scanner -----------------------//

    /** {@inheritDoc} */
    @Override
    public boolean hasModule(String name) {
        return modules.containsKey(name);
    }

    /**
     * Get a module given its name
     *
     * @param module Module name
     * @return Module
     * @throws ModuleNotDefinedException If the module is not defined
     */
    private Module getModule(String module) throws ModuleNotDefinedException {
        if (!hasModule(module))
            throw new ModuleNotDefinedException(module);
        return modules.get(module);
    }

    /**
     * Get all the modules
     *
     * @return All the configured modules
     */
    private Collection<Module> getModules() {
        return modules.values();
    }


    /**
     * {@inheritDoc}
     *
     * Get an action given its module, name and parameters
     */
    @Override
    public <U> Action getAction(String moduleName, String actionName, Map<String, U[]> params)
            throws ActionNotDefinedException, ModuleNotDefinedException {

        Action action = getModule(moduleName).getAction(actionName, params);
        if (action == null)
            throw new ActionNotDefinedException(actionName, params.keySet());
        return action;
    }

    /**
     * Create the help of the actions of the given module
     *
     * @return Help of the module
     */
    private List<ActionHelp> getHelp(Module module) {
        return module.getAllActions()
                .stream()
                .map(action -> new ActionHelp(module.getName(), action))
                .sorted((a1, a2) -> a1.getName().compareToIgnoreCase(a2.getName()))
                .collect(Collectors.toList());
    }

    /** {@inheritDoc} */
    @Override
    public List<ActionHelp> getHelp(String moduleName) throws ModuleNotDefinedException {
        return getHelp(getModule(moduleName));
    }

    /** {@inheritDoc} */
    @Override
    public List<ActionHelp> getHelp() {
        return getModules()
                .stream()
                .flatMap(m -> getHelp(m).stream())
                .sorted((a1, a2) -> a1.getName().compareToIgnoreCase(a2.getName()))
                .collect(Collectors.toList());
    }


    //----------------------- Dispatch an action -----------------------//

    /**
     * Try to force casts for integers, doubles, floats, longs and booleans
     *
     * @param paramValue        Parameter value
     * @param expectedParamType Expected type of the parameter
     * @return Casted parameter
     * @throws InvalidParameterTypeException If the parameter cannot be casted to the expected type
     */
    private Object castStringParameter(String paramValue, Class<?> expectedParamType)
            throws InvalidParameterTypeException {

        try {
            if (expectedParamType == Integer.class || expectedParamType == int.class)
                return Integer.valueOf(paramValue);
            if (expectedParamType == Double.class || expectedParamType == double.class)
                return Double.valueOf(paramValue);
            if (expectedParamType == Float.class || expectedParamType == float.class)
                return Float.valueOf(paramValue);
            if (expectedParamType == Long.class || expectedParamType == long.class)
                return Long.valueOf(paramValue);
            if (expectedParamType == Boolean.class || expectedParamType == boolean.class)
                return Boolean.valueOf(paramValue);
            if (expectedParamType.isAssignableFrom(paramValue.getClass()))
                return expectedParamType.cast(paramValue);
            return paramValue;
        } catch (Exception e) {
            throw new InvalidParameterTypeException(paramValue);
        }
    }

    /**
     * Cast an array
     *
     * @param paramValue        Parameter value
     * @param arrayComponentType Expected type of each array value
     * @return Casted parameter
     * @throws InvalidParameterTypeException If the parameter cannot be casted to the expected type
     */
    private Object castArrayParameter(Object paramValue, Class<?> arrayComponentType) throws InvalidParameterTypeException {

        Object[] paramValueArray = (Object[]) paramValue;

        // Create the output array
        Object output;
        try {
            if (arrayComponentType == int.class)
                output = new int[paramValueArray.length];
            else if (arrayComponentType == double.class)
                output = new double[paramValueArray.length];
            else if (arrayComponentType == float.class)
                output = new float[paramValueArray.length];
            else if (arrayComponentType == long.class)
                output = new long[paramValueArray.length];
            else if (arrayComponentType == boolean.class)
                output = new boolean[paramValueArray.length];
            else
                output = Array.newInstance(arrayComponentType, paramValueArray.length);
        } catch (Exception e) {
            throw new InvalidParameterTypeException(paramValue);
        }

        // Cast each member
        for (int i = 0; i < paramValueArray.length; i++) {
            Object value = paramValueArray[i];
            if (value instanceof String && arrayComponentType != String.class)
                value = castStringParameter((String) value, arrayComponentType);
            Array.set(output, i, value);
        }

        return output;
    }

    /**
     * Check if the parameter should be casted, and do it if necessary
     * If the expected type is not an array and the param value is, the first element will be taken
     *
     * @param paramValue        Parameter value
     * @param expectedParamType Expected type for the parameter
     * @return Parameter value, casted if necessary
     * @throws InvalidParameterTypeException If the parameter value has not the expected type
     */
    private Object castParameterValueIfNecessary(Object paramValue, Class<?> expectedParamType)
            throws InvalidParameterTypeException {

        if (expectedParamType.isArray())
            return castArrayParameter(paramValue, expectedParamType.getComponentType());

        if (paramValue.getClass().isArray())
            paramValue = ((Object[]) paramValue)[0];
        if (paramValue instanceof String && expectedParamType != String.class)
            paramValue = castStringParameter((String) paramValue, expectedParamType);
        return paramValue;
    }

    /**
     * Prepare the input of the action, this is, an ordered array of the parameters
     * If a parameter is the token, the module token will be injected into the input
     *
     * @param token  Token of the module
     * @param action Action
     * @param params Action parameters
     * @param <U>    Parameter types
     * @return Input for the action
     * @throws InvalidParameterTypeException If a supplied parameter has not the expected type
     */
    private <U> Object[] prepareInput(String token, Action action, Map<String, U[]> params)
            throws InvalidParameterTypeException {

        List<Object> input = new ArrayList<>(action.getParameters().size());
        for (Map.Entry<String, Class> entry : action.getParameters().entrySet()) {
            String paramName = entry.getKey();
            Class paramType = entry.getValue();
            if (paramName.equals(Action.TOKEN_PARAM)) {
                input.add(token);
                continue;
            }
            Object paramValue = castParameterValueIfNecessary(params.getOrDefault(paramName, null), paramType);
            input.add(paramValue);
        }
        return input.toArray();
    }

    /**
     * Dispatch an action
     *
     * @param token  Token of the module
     * @param action Action
     * @param params Action parameters
     * @param <T>    Type of the result of the action
     * @param <U>    Parameter types
     * @return Result of the action
     * @throws Exception Any exception thrown by the action
     */
    @SuppressWarnings("unchecked")
    private <T, U> T dispatch(String token, Action action, Map<String, U[]> params) throws Exception {
        if (action == null || params == null)
            throw new IllegalArgumentException("The parameters cannot be null");
        if (action.requiresToken() && (token == null || token.isEmpty()))
            throw new ModuleTokenNotFoundException("A valid token is required to executeAndFetch " + action.getName());
        Object[] input = prepareInput(token, action, params);
        try {
            return (T) action.invoke(input);
        } catch (InvocationTargetException e) {
            throw (Exception) e.getCause();
        }
    }

    /** {@inheritDoc} */
    @Override
    public <T, U> T dispatch(Session session, String moduleName, String actionName, Map<String, U[]> params) throws Exception {
        String token = session.getToken(moduleName);
        Action action = getAction(moduleName, actionName, params);
        return dispatch(token, action, params);
    }

    /** {@inheritDoc} */
    @Override
    public <T> T dispatch(Session session, String moduleName, String actionName) throws Exception {
        return dispatch(session, moduleName, actionName, new HashMap<>());
    }

}
