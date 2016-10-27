package io.reneses.tela.core.history.models;

import java.util.Arrays;

/**
 * History Entry model
 * A history entry is defined by the module, the action name, and the parameters it has been invokated with.
 */
public class HistoryEntry {

    private String module, action;
    private Object[] parameters;

    /**
     * Constructor for HistoryEntry.
     *
     * @param module a {@link java.lang.String} object.
     * @param action a {@link java.lang.String} object.
     * @param parameters a {@link java.lang.Object} object.
     */
    public HistoryEntry(String module, String action, Object... parameters) {
        this.module = module;
        this.action = action;
        this.parameters = parameters;
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
     * Getter for the field <code>action</code>.
     *
     * @return a {@link java.lang.String} object.
     */
    public String getAction() {
        return action;
    }

    /**
     * Getter for the field <code>parameters</code>.
     *
     * @return an array of {@link java.lang.Object} objects.
     */
    public Object[] getParameters() {
        return parameters;
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HistoryEntry that = (HistoryEntry) o;
        if (!module.equals(that.module)) return false;
        if (!action.equals(that.action)) return false;
        return Arrays.equals(parameters, that.parameters);
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        int result = module.hashCode();
        result = 31 * result + action.hashCode();
        result = 31 * result + Arrays.hashCode(parameters);
        return result;
    }

}
