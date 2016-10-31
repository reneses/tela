package io.reneses.tela.core.scheduler.exceptions;

import io.reneses.tela.core.api.exceptions.ApiException;
import io.reneses.tela.core.scheduler.models.ScheduledAction;

/**
 * Exception raised when trying to perform scheduled operations over a not scheduled action
 */
public class ActionNotScheduledException extends ApiException {

    /**
     * Constructor with instance
     *
     * @param action Scheduled Action
     */
    public ActionNotScheduledException(ScheduledAction action) {
        super("The action '" + action + "' is not scheduled", 404);
    }

    /**
     * Constructor with ID
     *
     * @param actionId Scheduled Action ID
     */
    public ActionNotScheduledException(int actionId) {
        super("There is not scheduled action with ID '" + actionId + "'", 404);
    }

}
