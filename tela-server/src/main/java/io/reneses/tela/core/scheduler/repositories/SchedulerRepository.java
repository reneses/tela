package io.reneses.tela.core.scheduler.repositories;

import io.reneses.tela.core.scheduler.models.ScheduledAction;

import java.util.List;

/**
 * Scheduler repository
 */
public interface SchedulerRepository {

    /**
     * Create an action
     *
     * @param action Scheduled action
     * @return Whether the action was created or not (if it was already existing or not)
     */
    boolean create(ScheduledAction action);

    /**
     * Update the next execution datetime of an action
     *
     * @param action Scheduled action
     */
    void updateNextExecution(ScheduledAction action);

    /**
     * Find an scheduled action by its ID
     *
     * @param actionId Scheduled action ID
     * @return Scheduled action
     */
    ScheduledAction find(int actionId);

    /**
     * Find all the scheduled actions that are ready to be executed
     * An action is ready if nextExecution is prior to this moment
     *
     * @return Scheduled actions read to be executed
     */
    List<ScheduledAction> findReadyToExecute();

    /**
     * Find all the scheduled actions with the given session
     *
     * @param accessToken Session
     * @return Scheduled actions with the given session
     */
    List<ScheduledAction> findByAccessToken(String accessToken);

    /**
     * Find all the scheduled actions
     *
     * @return Scheduled actions
     */
    List<ScheduledAction> findAll();

    /**
     * Check if the action is scheduled
     *
     * @param actionId Action ID
     * @return True if the action is scheduled, false otherwise
     */
    boolean contains(int actionId);

    /**
     * Delete an scheduled action
     *
     * @param actionId Action ID
     * @return True if deleted, false otherwise
     */
    boolean delete(int actionId);

    /**
     * Delete all the scheduled actions with a given session
     *
     * @param accessToken Session
     * @return Number of scheduled actions deleted
     */
    int deleteByAccessToken(String accessToken);

}
