package io.reneses.tela.core.scheduler;

import io.reneses.tela.core.dispatcher.ActionDispatcher;
import io.reneses.tela.core.sessions.SessionManager;

/**
 * Abstract Scheduler with common functionality for every implementation
 */
abstract class AbstractScheduler implements Scheduler {

    /**
     * The execution delay of the scheduler, given in seconds
     */
    private int delay;

    protected ActionDispatcher dispatcher;
    protected SessionManager sessionManager;

    /**
     * SchedulerImpl constructor
     *
     * @param dispatcher Action dispatcher
     * @param sessionManager Session manager
     * @param delay SchedulerImpl delay (in seconds)
     */
    AbstractScheduler(ActionDispatcher dispatcher, SessionManager sessionManager, int delay) {
        this.dispatcher = dispatcher;
        this.sessionManager = sessionManager;
        this.delay = delay;
    }

    /**
     * {@inheritDoc}
     *
     * @return a int.
     */
    public int getDelay() {
        return delay;
    }

}
