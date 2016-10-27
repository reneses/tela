package io.reneses.tela.core.dispatcher.exceptions;


/**
 * ActionBadDefinedException class.
 */
public class ActionBadDefinedException extends IllegalArgumentException {

    /**
     * Constructor for ActionBadDefinedException.
     *
     * @param actionName Action name
     */
    public ActionBadDefinedException(String actionName) {
        super("The action '" + actionName + "' is bad defined");
    }

    /**
     * Constructor for ActionBadDefinedException.
     *
     * @param actionName Action name
     * @param message Error message
     */
    public ActionBadDefinedException(String actionName, String message) {
        super("The action '" + actionName + "' is bad defined: " + message);
    }
}
