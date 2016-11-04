package io.reneses.tela.core.scheduler;

import io.reneses.tela.core.dispatcher.ActionDispatcher;
import io.reneses.tela.core.dispatcher.exceptions.*;
import io.reneses.tela.core.scheduler.exceptions.ActionAlreadyScheduledException;
import io.reneses.tela.core.scheduler.exceptions.ActionNotSchedulableException;
import io.reneses.tela.core.scheduler.exceptions.ActionNotScheduledException;
import io.reneses.tela.core.scheduler.exceptions.ForbiddenSchedulerException;
import io.reneses.tela.core.scheduler.models.ScheduledAction;
import io.reneses.tela.core.scheduler.repositories.OrientSchedulerRepository;
import io.reneses.tela.core.dispatcher.models.Action;
import io.reneses.tela.core.scheduler.repositories.SchedulerRepository;
import io.reneses.tela.core.sessions.SessionManager;
import io.reneses.tela.core.sessions.models.Session;
import io.reneses.tela.core.sessions.exceptions.ModuleTokenNotFoundException;
import io.reneses.tela.core.sessions.exceptions.SessionNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.concurrent.*;

/**
 * Action SchedulerImpl
 */
class SchedulerImpl extends AbstractScheduler {

    private static final Logger LOGGER = LoggerFactory.getLogger(SchedulerImpl.class);

    private SchedulerRepository repository;
    private ScheduledExecutorService pool;

    /**
     * Scheduler constructor
     *
     * @param dispatcher     Action dispatcher
     * @param sessionManager Session manager
     * @param delay          Scheduler delay (in seconds)
     */
    SchedulerImpl(ActionDispatcher dispatcher, SessionManager sessionManager, int delay) {
        super(dispatcher, sessionManager, delay);
        this.repository = new OrientSchedulerRepository();
        this.pool = Executors.newScheduledThreadPool(1);
        this.pool.scheduleAtFixedRate(this::executor, getDelay(), getDelay(), TimeUnit.SECONDS);
        LOGGER.info("[Scheduler] Scheduler initiated with a delay of {} seconds", getDelay());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close() {
        pool.shutdown();
    }

    /**
     * Executor of the scheduler
     * The executor will getId all the actions from the repository and execute them
     */
    private void executor() {
        LOGGER.debug("Scheduler executed");
        repository.findReadyToExecute().parallelStream().forEach(task -> {
            try {
                LOGGER.info("[Scheduler] Executing scheduled task: {} with ID {}, next: {}",
                        task, task.getId(), task.getNextExecution());
                executeScheduled(task);
                task.updateNextExecution();
                repository.updateNextExecution(task);
            } catch (ActionNotDefinedException | ActionNotSchedulableException |
                    ModuleNotDefinedException | InvalidParameterTypeException e) {
                throw new RuntimeException("The scheduled action " + task + " has been corrupted");
            } catch (SessionNotFoundException | ModuleTokenNotFoundException e) {
                LOGGER.warn("[Scheduler] The session {} is not available anymore, any of its actions will be rescheduled",
                        task.getAccessToken());
                repository.deleteByAccessToken(task.getAccessToken());
            } catch (Exception e) {
                LOGGER.error("[Scheduler] The scheduled action has raised an exception and will not be rescheduled again", e);
                repository.delete(task.getId());
            }
        });
    }

    /**
     * Execute an scheduled action
     *
     * @param scheduledAction Scheduled action
     * @param <T>             Return type of the action
     * @return Result of the action
     * @throws ActionNotDefinedException     Action is not defined
     * @throws InvocationTargetException     The action could not be executed
     * @throws ActionNotSchedulableException The action is not marked as schedulable
     * @throws SessionNotFoundException      The session of the action is not valid anymore
     */
    private <T> T executeScheduled(ScheduledAction scheduledAction)
            throws Exception {

        if (scheduledAction == null)
            throw new IllegalArgumentException("Scheduled action cannot be null");

        Action action = dispatcher.getAction(scheduledAction.getModuleName(), scheduledAction.getActionName(), scheduledAction.getParams());
        Session session = sessionManager.findByAccessToken(scheduledAction.getAccessToken());

        if (!action.isSchedulable())
            throw new ActionNotSchedulableException(action);

        if (scheduledAction.getDelay() < action.getMinimumScheduleDelay()) {
            LOGGER.warn("[Scheduler] The task '{}/{}' delay {}s has been increased to the action's minimum {}s",
                    scheduledAction.getModuleName(), scheduledAction.getActionName(), scheduledAction.getDelay(), action.getMinimumScheduleDelay());
            scheduledAction.setDelay(action.getMinimumScheduleDelay());
        }

        return dispatcher.dispatch(session, scheduledAction.getModuleName(), action.getName(), scheduledAction.getParams());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T schedule(ScheduledAction action) throws Exception {
        T result = executeScheduled(action);
        if (!repository.create(action))
            throw new ActionAlreadyScheduledException(action);
        LOGGER.info("[Scheduler] Scheduled action: {} with ID {}, next: {}",
                action, action.getId(), action.getNextExecution());
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean contains(ScheduledAction action) {
        return repository.contains(action.getId());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ScheduledAction getScheduledAction(int actionId) throws ActionNotScheduledException {
        ScheduledAction action = repository.find(actionId);
        if (action == null)
            throw new ActionNotScheduledException(actionId);
        return action;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ScheduledAction> getScheduledActions(Session session) {
        return repository.findByAccessToken(session.getAccessToken());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ScheduledAction> getScheduledActions() {
        return repository.findAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void cancel(Session session, ScheduledAction action) throws ActionNotScheduledException, ForbiddenSchedulerException {
        if (!session.getAccessToken().equals(action.getAccessToken()))
            throw new ForbiddenSchedulerException(session.getId(), action);
        if (!repository.delete(action.getId()))
            throw new ActionNotScheduledException(action);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void cancel(Session session, int actionId) throws ActionNotScheduledException, ForbiddenSchedulerException {
        cancel(session, getScheduledAction(actionId));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void cancelAll(Session session) {
        repository.deleteByAccessToken(session.getAccessToken());
    }

}
