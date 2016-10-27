package io.reneses.tela.core.dispatcher.models;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Module class
 * A module represents a group of actions
 */
public class Module {

    private String name;
    private Map<String, List<Action>> actions = new HashMap<>();

    /**
     * Construct the instance given its name
     *
     * @param name Module name
     */
    public Module(String name) {
        this.name = normalizeName(name);
    }

    /**
     * Normalize the name
     *
     * @param name Module name
     * @return Normalized name
     */
    private String normalizeName(String name) {
        return name.toLowerCase().replace("[\\s/]", "-");
    }

    /**
     * Get the module name
     *
     * @return Name
     */
    public String getName() {
        return name;
    }

    /**
     * Add an action to the module
     *
     * @param action Action to include
     */
    public void addAction(Action action) {
        String name = action.getName().toLowerCase();
        actions.putIfAbsent(name, new ArrayList<>());
        actions.get(name).add(action);
    }

    /**
     * Add all the actions
     *
     * @param actions Actions to be added
     */
    public void addActions(Iterable<Action> actions) {
        actions.forEach(this::addAction);
    }

    /**
     * Get all the actions
     *
     * @return Module actions
     */
    public List<Action> getAllActions() {
        return actions
                .values()
                .stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    /**
     * Get all the actions with a given name
     *
     * @param name Action name
     * @return List of actions with the given name
     */
    public List<Action> getActions(String name) {
        return actions.getOrDefault(normalizeName(name), new ArrayList<>());
    }

    /**
     * Get an action given its name and parameters
     *
     * @param name   Action name
     * @param params Name of its parameters
     * @return Action if found, null otherwise
     */
    public Action getAction(String name, String... params) {

        Set<String> paramsSet = new HashSet<>(Arrays.asList(params));
        for (Action action : getActions(name)) {

            // Do not take the token param into account
            if (action.requiresToken())
                paramsSet.add(Action.TOKEN_PARAM);

            // Check that they have the same number of params
            if (paramsSet.size() != action.getParameters().size())
                continue;

            // Check that every param matches
            boolean paramsMatch = true;
            for (String param : params) {
                if (!action.hasParameter(param)) {
                    paramsMatch = false;
                    break;
                }
            }

            if (paramsMatch)
                return action;
        }

        // If no action was found, return null
        return null;

    }

    /**
     * Get an action given its name and its parameters
     *
     * @param actionName Action name
     * @param params     Parameters of the action
     * @param <U>        Type of the parameters of the action
     * @return Action if found, null otherwise
     */
    public <U> Action getAction(String actionName, Map<String, U[]> params) {
        String[] inputParamNames = params.keySet().toArray(new String[params.size()]);
        return getAction(actionName, inputParamNames);
    }

}
