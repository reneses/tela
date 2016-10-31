package io.reneses.tela.core.scheduler.exceptions;

import io.reneses.tela.core.api.exceptions.ApiException;
import io.reneses.tela.core.scheduler.models.ScheduledAction;

/**
 * Exception raised when scheduling an action that is already scheduled
 */
public class ActionAlreadyScheduledException extends ApiException {

    /**
     * Constructor
     *
     * @param action Scheduled action
     */
    public ActionAlreadyScheduledException(ScheduledAction action) {
        super("The scheduled action '" + action + "' is already scheduled from the same session", 409);
    }

}
