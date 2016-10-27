package io.reneses.tela.core.scheduler.exceptions;

import io.reneses.tela.core.api.exceptions.ApiException;
import io.reneses.tela.core.scheduler.models.ScheduledAction;

/**
 * Exception raised when an action not schedulable tries to be scheduled
 */
public class ForbiddenSchedulerException extends ApiException {

    /**
     * Constructor
     *
     * @param session Session trying to modify the action
     * @param action  Scheduled action
     */
    public ForbiddenSchedulerException(String session, ScheduledAction action) {
        super("The scheduled action '" + action + "' is not modifiable from session '" + session + '"', 403);
    }

}
