package io.reneses.tela.core.dispatcher;

import io.reneses.tela.core.dispatcher.exceptions.ActionNotDefinedException;
import io.reneses.tela.core.dispatcher.exceptions.ModuleNotDefinedException;
import io.reneses.tela.core.dispatcher.models.Action;
import io.reneses.tela.core.dispatcher.models.ActionHelp;
import io.reneses.tela.core.sessions.models.Session;

import java.util.List;
import java.util.Map;

/**
 * Dispatcher for the actions
 */
public interface ActionDispatcher {

    /**
     * Check if a module has been defined
     *
     * @param name Module name
     * @return True if it has, false otherwise
     */
    boolean hasModule(String name);

    /**
     * Get an action given its module, name and parameters
     *
     * @param moduleName Module name
     * @param actionName Action name
     * @param params     Map of parameters
     * @param <U>        Parameter types
     * @return Action
     * @throws ModuleNotDefinedException If the module is not defined
     * @throws ActionNotDefinedException If the action is not defined within the desired module
     */
    <U> Action getAction(String moduleName, String actionName, Map<String, U[]> params)
            throws ActionNotDefinedException, ModuleNotDefinedException;

    /**
     * Obtain the auto-generated help
     *
     * @return Help about the actions
     */
    List<ActionHelp> getHelp();

    /**
     * Obtain the auto-generated help of a specific module
     *
     * @return Help about the actions of the given module
     * @param module Module to obtain the help from
     * @throws ModuleNotDefinedException if any.
     */
    List<ActionHelp> getHelp(String module) throws ModuleNotDefinedException;

    /**
     * Dispatch an action
     *
     * @param session    Session
     * @param moduleName Name of the module of the action
     * @param actionName Name of the action
     * @param params     Action parameters
     * @param <T>        Type of the result of the action
     * @param <U>        Parameter types
     * @return Result of the action
     * @throws Exception Any exception thrown by the action
     */
    <T, U> T dispatch(Session session, String moduleName, String actionName, Map<String, U[]> params) throws Exception;

    /**
     * Dispatch an action without parameters
     *
     * @param session    Session
     * @param moduleName Name of the module of the action
     * @param actionName Name of the action
     * @param <T>        Type of the result of the action
     * @return Result of the action
     * @throws Exception Any exception thrown by the action
     */
    <T> T dispatch(Session session, String moduleName, String actionName) throws Exception;

}
