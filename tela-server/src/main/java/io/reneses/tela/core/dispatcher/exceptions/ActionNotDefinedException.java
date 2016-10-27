package io.reneses.tela.core.dispatcher.exceptions;

import io.reneses.tela.core.api.exceptions.ApiException;

import java.util.Set;

/**
 * ActionNotDefinedException class.
 */
public class ActionNotDefinedException extends ApiException {

    /**
     * Constructor for ActionNotDefinedException.
     */
    public ActionNotDefinedException() {
        super("The action requested is not defined", 404);
    }

    /**
     * Constructor for ActionNotDefinedException.
     *
     * @param actionName Action name
     */
    public ActionNotDefinedException(String actionName) {
        super("The action '" + actionName + "' is not defined", 404);
    }

    /**
     * Constructor for ActionNotDefinedException.
     *
     * @param actionName Action name
     * @param params Action parameters
     */
    public ActionNotDefinedException(String actionName, Set<String> params) {
        super("The action '" + actionName + "' is not defined for the following parameters: " + params, 404);
    }

}
