package io.reneses.tela.modules.instagram.api.models;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Pagination class.
 */
public class Pagination {

    @JsonProperty("next_url")
    private String nextUrl;

    @JsonProperty("next_cursor")
    private String nextCursor;

    @JsonProperty("next_max_id")
    private String nextMaxId;

    /**
     * Getter for the field <code>nextUrl</code>.
     *
     * @return a {@link java.lang.String} object.
     */
    public String getNextUrl() {
        return nextUrl;
    }

    /**
     * Setter for the field <code>nextUrl</code>.
     *
     * @param nextUrl a {@link java.lang.String} object.
     */
    public void setNextUrl(String nextUrl) {
        this.nextUrl = nextUrl;
    }

    /**
     * Getter for the field <code>nextCursor</code>.
     *
     * @return a {@link java.lang.String} object.
     */
    public String getNextCursor() {
        return nextCursor;
    }

    /**
     * Setter for the field <code>nextCursor</code>.
     *
     * @param nextCursor a {@link java.lang.String} object.
     */
    public void setNextCursor(String nextCursor) {
        this.nextCursor = nextCursor;
    }

    public String getNextMaxId() {
        return nextMaxId;
    }

    public void setNextMaxId(String nextMaxId) {
        this.nextMaxId = nextMaxId;
    }

    /**
     * hasNext.
     *
     * @return a boolean.
     */
    public boolean hasNext() {
        return nextUrl != null;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return "Pagination{" +
                "nextUrl='" + nextUrl + '\'' +
                ", nextCursor='" + nextCursor + '\'' +
                ", nextMaxId='" + nextMaxId + '\'' +
                '}';
    }
}
