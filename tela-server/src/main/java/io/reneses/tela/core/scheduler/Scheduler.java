package io.reneses.tela.core.scheduler;

import io.reneses.tela.core.dispatcher.exceptions.ActionNotDefinedException;
import io.reneses.tela.core.scheduler.exceptions.ActionAlreadyScheduledException;
import io.reneses.tela.core.scheduler.exceptions.ActionNotSchedulableException;
import io.reneses.tela.core.scheduler.exceptions.ActionNotScheduledException;
import io.reneses.tela.core.scheduler.exceptions.ForbiddenSchedulerException;
import io.reneses.tela.core.scheduler.models.ScheduledAction;
import io.reneses.tela.core.sessions.exceptions.SessionNotFoundException;
import io.reneses.tela.core.sessions.models.Session;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * Action Scheduler
 */
public interface Scheduler {

    /**
     * The execution delay of the scheduler, given in seconds
     */
    int DEFAULT_DELAY = 300;

    /**
     * Get the execution delay
     *
     * @return The execution delay of the scheduler, given in seconds
     */
    int getDelay();

    /**
     * Stop and close the scheduler (the scheduled actions, however, will not be removed from the data storage
     */
    void close();

    /**
     * Schedule an action
     *
     * @param action Action
     * @param <T>    Return type of the action
     * @return Result of the action
     * @throws ActionNotDefinedException       Action is not defined
     * @throws InvocationTargetException       The action could not be executed
     * @throws ActionNotSchedulableException   The action is not marked as schedulable
     * @throws SessionNotFoundException        The session is not valid
     * @throws ActionAlreadyScheduledException The action has already been scheduled for the same session
     * @throws Exception                       if any.
     */
    <T> T schedule(ScheduledAction action) throws Exception;

    /**
     * Check if the scheduler has a given action
     *
     * @param action Scheduled action
     * @return True if it is contained, False otherwise
     */
    boolean contains(ScheduledAction action);

    /**
     * Get the scheduled action by ID
     *
     * @param actionId Action ID
     * @return Scheduled action
     * @throws ActionNotScheduledException There is not scheduled action for the given ID
     */
    ScheduledAction getScheduledAction(int actionId) throws ActionNotScheduledException;

    /**
     * Get all the scheduled actions by session
     *
     * @param session Session
     * @return Scheduled actions with the given session
     */
    List<ScheduledAction> getActions(Session session);

    /**
     * Get all the scheduled actions
     *
     * @return Scheduled actions
     */
    List<ScheduledAction> getActions();

    /**
     * Cancel a scheduled action
     *
     * @param session Session to cancel the action from
     * @param action  Action to be removed
     * @throws ActionNotScheduledException The provided action is not scheduled
     * @throws ForbiddenSchedulerException The provided action is scheduled with from other session
     */
    void cancel(Session session, ScheduledAction action) throws ActionNotScheduledException, ForbiddenSchedulerException;

    /**
     * Cancel the scheduled action given with the given id
     *
     * @param session  Session to cancel the action from
     * @param actionId Action ID
     * @throws ActionNotScheduledException The provided action is not scheduled
     * @throws ForbiddenSchedulerException The provided action is scheduled with from other session
     */
    void cancel(Session session, int actionId) throws ActionNotScheduledException, ForbiddenSchedulerException;

    /**
     * Cancel all the scheduled actions given with the provided session
     *
     * @param session Session
     */
    void cancelAll(Session session);

}
