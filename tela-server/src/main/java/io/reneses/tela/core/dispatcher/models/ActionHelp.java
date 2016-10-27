package io.reneses.tela.core.dispatcher.models;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Action help data class
 */
public class ActionHelp {

    @JsonProperty("module")
    private String module;

    @JsonProperty("name")
    private String name;

    @JsonProperty("params")
    private String[] params;

    @JsonProperty("description")
    private String description;

    /**
     * Constructor for ActionHelp.
     */
    public ActionHelp() {
    }

    /**
     * Constructor for ActionHelp.
     *
     * @param module Module name
     * @param action Action
     */
    public ActionHelp(String module, Action action) {
        this.module = module;
        this.name = action.getName();
        this.params = action.getParameters()
                .entrySet()
                .stream()
                .map(e -> e.getKey() + ": " + e.getValue().getCanonicalName().replace("java.lang.", ""))
                .toArray(String[]::new);
        this.description = action.getDescription();
    }

    /**
     * Getter for the field <code>module</code>.
     *
     * @return a {@link java.lang.String} object.
     */
    public String getModule() {
        return module;
    }

    /**
     * Setter for the field <code>module</code>.
     *
     * @param module a {@link java.lang.String} object.
     */
    public void setModule(String module) {
        this.module = module;
    }

    /**
     * Getter for the field <code>name</code>.
     *
     * @return a {@link java.lang.String} object.
     */
    public String getName() {
        return name;
    }

    /**
     * Setter for the field <code>name</code>.
     *
     * @param name a {@link java.lang.String} object.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter for the field <code>params</code>.
     *
     * @return an array of {@link java.lang.String} objects.
     */
    public String[] getParams() {
        return params;
    }

    /**
     * Setter for the field <code>params</code>.
     *
     * @param params an array of {@link java.lang.String} objects.
     */
    public void setParams(String[] params) {
        this.params = params;
    }

    /**
     * Getter for the field <code>description</code>.
     *
     * @return a {@link java.lang.String} object.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Setter for the field <code>description</code>.
     *
     * @param description a {@link java.lang.String} object.
     */
    public void setDescription(String description) {
        this.description = description;
    }

}
