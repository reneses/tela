package io.reneses.tela.core.dispatcher.models;


import io.reneses.tela.core.dispatcher.exceptions.ActionBadDefinedException;
import io.reneses.tela.core.dispatcher.annotations.Schedulable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;

/**
 * Action class
 */
public class Action {

    /** Constant <code>TOKEN_PARAM="token"</code> */
    public static final String TOKEN_PARAM = "token";

    private Method method;
    private String name, description;
    private Object instance;

    private boolean isSchedulable;
    private int minimumScheduleDelay;

    // Map of parameters, with the form 'name: type'
    private LinkedHashMap<String, Class> parameters;

    /**
     * Action constructor. It:
     * 1. Checks if the method is annotated as an action
     * 2. Saves the method
     * 3. Loads the action properties
     * 4. Load and parse the parameters
     * 5. Creates an instance of the method's class
     * 6. Save if it is schedulable or not (and, if so, its minimum delay)
     *
     * @param method Method
     */
    public Action(Method method) {
        if (!method.isAnnotationPresent(io.reneses.tela.core.dispatcher.annotations.Action.class))
            throw new ActionBadDefinedException(method.getName(), " The method is not annotated with @Action!");
        this.method = method;
        io.reneses.tela.core.dispatcher.annotations.Action annotation =
                method.getDeclaredAnnotation(io.reneses.tela.core.dispatcher.annotations.Action.class);
        this.name = (annotation.name().isEmpty() ? method.getName() : annotation.name()).toLowerCase();
        this.description = annotation.description();
        this.parameters = parseParameters(annotation, method);
        try {
            instance = method.getDeclaringClass().newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new ActionBadDefinedException(name, "The action class does not have a public default constructor");
        }
        if (method.isAnnotationPresent(Schedulable.class)) {
            isSchedulable = true;
            minimumScheduleDelay = method.getDeclaredAnnotation(Schedulable.class).minimumDelay();
        }
    }

    /**
     * Load and parse the parameters
     *
     * @param annotation Annotation
     * @param method     Method
     * @return Parameters ordered map
     */
    private LinkedHashMap<String, Class> parseParameters(
            io.reneses.tela.core.dispatcher.annotations.Action annotation, Method method) {

        // Load the parameters
        String[] parameterNames = annotation.parameters();
        Class[] parameterTypes = method.getParameterTypes();

        // Check that the number of parameters is the same
        if (parameterNames.length != parameterTypes.length) {
            throw new IllegalArgumentException(String.format("The number of parameters annotated (%d) in the method " +
                            "'%s' is different to the number of actual parameters (%d)",
                    parameterNames.length, method.getName(), parameterTypes.length));
        }

        // Store the params
        LinkedHashMap<String, Class> out = new LinkedHashMap<>();
        for (int i = 0; i < parameterNames.length; i++) {
            out.put(parameterNames[i], parameterTypes[i]);
        }
        return out;
    }

    /**
     * Invoke the method
     *
     * @param params Parameters
     * @return Result
     * @throws InvocationTargetException If the invocation throws an exception
     */
    public Object invoke(Object[] params) throws InvocationTargetException {
        try {
            return method.invoke(instance, params);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Check if the action has a parameter
     *
     * @param name Parameter name
     * @return True if it does, false otherwise
     */
    public boolean hasParameter(String name) {
        return parameters.containsKey(name);
    }

    /**
     * Check if the action requires a session token
     *
     * @return True if it does, false otherwise
     */
    public boolean requiresToken() {
        return hasParameter(TOKEN_PARAM);
    }

    /**
     * Get the parameters
     *
     * @return Ordered map of parameters
     */
    public LinkedHashMap<String, Class> getParameters() {
        return parameters;
    }

    /**
     * Get the action's name
     *
     * @return Action's name
     */
    public String getName() {
        return name;
    }

    /**
     * Get the actions' description
     *
     * @return Action's description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Check if the action is schedulable
     *
     * @return True if it is, false otherwise
     */
    public boolean isSchedulable() {
        return isSchedulable;
    }

    /**
     * Return the minimum delay for the action
     *
     * @return Minimum delay for scheduling
     */
    public int getMinimumScheduleDelay() {
        return minimumScheduleDelay;
    }

}
