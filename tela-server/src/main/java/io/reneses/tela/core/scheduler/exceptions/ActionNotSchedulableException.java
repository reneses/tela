package io.reneses.tela.core.scheduler.exceptions;

import io.reneses.tela.core.api.exceptions.ApiException;
import io.reneses.tela.core.dispatcher.models.Action;

/**
 * Exception raised when an action not schedulable tries to be scheduled
 */
public class ActionNotSchedulableException extends ApiException {

    /**
     * Constructor
     *
     * @param action Action
     */
    public ActionNotSchedulableException(Action action) {
        super("The action '" + action.getName() + "' is not schedulable", 404);
    }

}
