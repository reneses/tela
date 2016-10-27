package io.reneses.tela.core.scheduler;

import io.reneses.tela.core.dispatcher.ActionDispatcher;
import io.reneses.tela.core.sessions.SessionManager;

/**
 * SchedulerImpl Factory responsible of creating an implementation of AbstractScheduler
 */
public class SchedulerFactory {

    private static int delay = Scheduler.DEFAULT_DELAY;

    private SchedulerFactory(){}

    /**
     * Set the scheduler delay
     *
     * @param schedulerDelay SchedulerImpl delay (in seconds)
     */
    public static void setDelay(int schedulerDelay) {
        if (schedulerDelay < 1)
            throw new IllegalArgumentException("Invalid delay value: " + schedulerDelay);
        delay = schedulerDelay;
    }

    /**
     * Create and return an instance of an implementation ofAbstract SchedulerImpl
     *
     * @param dispatcher Action Dispatcher
     * @param sessionManager Session Manager
     * @return SchedulerImpl
     */
    public static Scheduler create(ActionDispatcher dispatcher, SessionManager sessionManager) {
        return new SchedulerImpl(dispatcher, sessionManager, delay);
    }

}
