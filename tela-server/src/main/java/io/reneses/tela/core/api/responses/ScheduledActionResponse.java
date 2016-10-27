package io.reneses.tela.core.api.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.reneses.tela.core.scheduler.models.ScheduledAction;

/**
 * Response given by the scheduler
 * Once an action is scheduled, the API return its result and the scheduled action
 *
 * @param <T> Result type of the scheduled action
 */
public class ScheduledActionResponse<T> {

    @JsonProperty("scheduledAction")
    private ScheduledAction action;

    @JsonProperty("result")
    private T result;

    /**
     * Constructor for ScheduledActionResponse.
     * Needed for deserialization
     */
    public ScheduledActionResponse() {
    }

    /**
     * Constructor for ScheduledActionResponse.
     *
     * @param action a {@link io.reneses.tela.core.scheduler.models.ScheduledAction} object.
     * @param result a T object.
     */
    public ScheduledActionResponse(ScheduledAction action, T result) {
        super();
        this.action = action;
        this.result = result;
    }

    /**
     * Getter for the field <code>action</code>.
     *
     * @return a {@link io.reneses.tela.core.scheduler.models.ScheduledAction} object.
     */
    public ScheduledAction getAction() {
        return action;
    }

    /**
     * Setter for the field <code>action</code>.
     *
     * @param action a {@link io.reneses.tela.core.scheduler.models.ScheduledAction} object.
     */
    public void setAction(ScheduledAction action) {
        this.action = action;
    }

    /**
     * Getter for the field <code>result</code>.
     *
     * @return a T object.
     */
    public T getResult() {
        return result;
    }

    /**
     * Setter for the field <code>result</code>.
     *
     * @param result a T object.
     */
    public void setResult(T result) {
        this.result = result;
    }

}
