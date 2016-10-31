package io.reneses.tela.core.scheduler.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Scheduled Action
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ScheduledAction {

    private int id, delay;
    private String accessToken;
    private Map<String, String[]> params;
    private LocalDateTime createdAt, nextExecution;

    @JsonProperty("module")
    private String moduleName;

    @JsonProperty("action")
    private String actionName;

    /**
     * Init the object
     *
     * @param accessToken       Session
     * @param delay         Delay (in seconds) for the execution
     * @param actionPrefix  Action prefix
     * @param actionName    Action name
     * @param params        Action parameters
     * @param createdAt     Creation date of the scheduled action
     * @param nextExecution Datetime of the next execution
     */
    private void init(String accessToken, int delay, String actionPrefix, String actionName, Map<String, String[]> params,
                      LocalDateTime createdAt, LocalDateTime nextExecution) {
        this.accessToken = accessToken;
        this.delay = delay;
        this.moduleName = actionPrefix;
        this.actionName = actionName;
        this.params = params;
        this.createdAt = createdAt;
        this.nextExecution = nextExecution;
        this.id = hashCode();
    }

    /**
     * Default constructor for deserialization purposes
     */
    public ScheduledAction() {
        // Do not use programmatically
    }

    /**
     * Full constructor
     *
     * @param accessToken       Session
     * @param delay         Delay (in seconds) for the execution
     * @param moduleName  Action prefix
     * @param actionName    Action name
     * @param params        Action parameters
     * @param createdAt     Creation date of the scheduled action
     * @param nextExecution Datetime of the next execution
     */
    public ScheduledAction(String accessToken, int delay, String moduleName, String actionName, Map<String, String[]> params,
                           LocalDateTime createdAt, LocalDateTime nextExecution) {
        init(accessToken, delay, moduleName, actionName, params, createdAt, nextExecution);
    }

    /**
     * Constructor for first-time created objects
     * It initiates the created date and calculates the next execution
     *
     * @param accessToken      Session
     * @param delay        Delay (in seconds) for the execution
     * @param moduleName Action prefix
     * @param actionName   Action name
     * @param params       Action parameters
     */
    public ScheduledAction(String accessToken, int delay, String moduleName, String actionName, Map<String, String[]> params) {
        LocalDateTime now = LocalDateTime.now();
        init(accessToken, delay, moduleName, actionName, params, now, now.plusSeconds(delay));
    }

    /**
     * Constructor for first-time created objects without parameters
     * It initiates the created date and calculates the next execution
     *
     * @param accessToken      Session
     * @param delay        Delay (in seconds) for the execution
     * @param moduleName Action prefix
     * @param actionName   Action name
     */
    public ScheduledAction(String accessToken, int delay, String moduleName, String actionName) {
        this(accessToken, delay, moduleName, actionName, new HashMap<>());
    }

    //----------------------------------- OTHER -----------------------------------//

    /**
     * Set the ID property
     *
     * @param id Scheduled action ID
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Get the ID property
     *
     * @return Scheduled action ID
     */
    public int getId() {
        return this.id;
    }

    /**
     * Update the date of the next execution
     */
    public void updateNextExecution() {
        if (delay <= 0)
            throw new IllegalStateException("The delay must be a positive integer");
        LocalDateTime now = LocalDateTime.now();
        do {
            nextExecution = nextExecution.plusSeconds(delay);
        }
        while (!nextExecution.isAfter(now));
    }

    //----------------------------------- TIME JSON PROPERTIES -----------------------------------//

    /**
     * setCreatedAtAsLong.
     *
     * @param createdAt a long.
     */
    @JsonProperty("createdAt")
    public void setCreatedAtAsLong(long createdAt) {
        this.createdAt = LocalDateTime.ofInstant(Instant.ofEpochMilli(createdAt), TimeZone.getDefault().toZoneId());
    }
    /**
     * getCreatedAtAsLong.
     *
     * @return a long.
     */
    @JsonProperty("createdAt")
    public long getCreatedAtAsLong() {
        return createdAt.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    /**
     * setNextExecutionAsLong.
     *
     * @param nextExecution a long.
     */
    @JsonProperty("nextExecution")
    public void setNextExecutionAsLong(long nextExecution) {
        this.nextExecution = LocalDateTime.ofInstant(Instant.ofEpochMilli(nextExecution), TimeZone.getDefault().toZoneId());
    }
    /**
     * getNextExecutionAsLong.
     *
     * @return a long.
     */
    @JsonProperty("nextExecution")
    public long getNextExecutionAsLong() {
        return nextExecution.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    //----------------------------------- SETTERS AND GETTERS -----------------------------------//

    /**
     * Getter for the field <code>delay</code>.
     *
     * @return a int.
     */
    public int getDelay() {
        return delay;
    }

    /**
     * Setter for the field <code>delay</code>.
     *
     * @param delay a int.
     */
    public void setDelay(int delay) {
        this.delay = delay;
    }

    /**
     * Getter for the field <code>accessToken</code>.
     *
     * @return a {@link java.lang.String} object.
     */
    public String getAccessToken() {
        return accessToken;
    }

    /**
     * Setter for the field <code>accessToken</code>.
     *
     * @param accessToken a {@link java.lang.String} object.
     */
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    /**
     * Getter for the field <code>moduleName</code>.
     *
     * @return a {@link java.lang.String} object.
     */
    public String getModuleName() {
        return moduleName;
    }

    /**
     * Setter for the field <code>moduleName</code>.
     *
     * @param moduleName a {@link java.lang.String} object.
     */
    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    /**
     * Getter for the field <code>actionName</code>.
     *
     * @return a {@link java.lang.String} object.
     */
    public String getActionName() {
        return actionName;
    }

    /**
     * Setter for the field <code>actionName</code>.
     *
     * @param actionName a {@link java.lang.String} object.
     */
    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    /**
     * Getter for the field <code>createdAt</code>.
     *
     * @return a {@link java.time.LocalDateTime} object.
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * Setter for the field <code>createdAt</code>.
     *
     * @param createdAt a {@link java.time.LocalDateTime} object.
     */
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Getter for the field <code>nextExecution</code>.
     *
     * @return a {@link java.time.LocalDateTime} object.
     */
    public LocalDateTime getNextExecution() {
        return nextExecution;
    }

    /**
     * Setter for the field <code>nextExecution</code>.
     *
     * @param nextExecution a {@link java.time.LocalDateTime} object.
     */
    public void setNextExecution(LocalDateTime nextExecution) {
        this.nextExecution = nextExecution;
    }

    /**
     * Getter for the field <code>params</code>.
     *
     * @return a {@link java.util.Map} object.
     */
    public Map<String, String[]> getParams() {
        return params;
    }

    /**
     * Setter for the field <code>params</code>.
     *
     * @param params a {@link java.util.Map} object.
     */
    public void setParams(Map<String, String[]> params) {
        this.params = params;
    }


    //----------------------------------- OBJECT OVERRIDE -----------------------------------//

    /** {@inheritDoc} */
    @Override
    public String toString() {
        String paramsString = params
                .entrySet()
                .stream()
                .map(entry -> entry.getKey() + ": " + Arrays.toString(entry.getValue()))
                .collect(Collectors.toList())
                .toString();
        return moduleName + "/" + actionName + " (" + delay + "s) " + paramsString;
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ScheduledAction)) return false;
        ScheduledAction that = (ScheduledAction) o;
        if (delay != that.delay) return false;
        if (!accessToken.equals(that.accessToken)) return false;
        if (!moduleName.equals(that.moduleName)) return false;
        if (!actionName.equals(that.actionName)) return false;
        if (params.size() != that.params.size()) return false;
        for (Map.Entry<String, String[]> entry : params.entrySet()) {
            if (!that.params.containsKey(entry.getKey())) return false;
            if (!Arrays.equals(entry.getValue(), that.params.get(entry.getKey()))) return false;
        }
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        int result = delay;
        result = 31 * result + accessToken.hashCode();
        result = 31 * result + moduleName.hashCode();
        result = 31 * result + actionName.hashCode();
        int mapHashCode = 0;
        for (Map.Entry<String, String[]> entry : params.entrySet())
            mapHashCode += entry.getKey().hashCode() ^ Arrays.hashCode(entry.getValue());
        result = 31 * result + mapHashCode;
        return result;
    }

}
