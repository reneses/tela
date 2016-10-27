package io.reneses.tela.modules.instagram.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

/**
 * UserRelationship class.
 */
public class UserRelationship {

    public enum Outgoing {

        FOLLOWS("follows"), REQUESTED("requested"), NONE("none");

        private final String value;

        Outgoing(final String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value;
        }

        public static Outgoing fromString(String text) {
            if (text != null) {
                for (Outgoing b : Outgoing.values()) {
                    if (text.equalsIgnoreCase(b.value)) {
                        return b;
                    }
                }
            }
            throw new IllegalArgumentException("No constant with text " + text + " found");
        }
    }

    public enum Incoming {

        FOLLOWED_BY("followed_by"), REQUESTED_BY("requested_by"), BLOCKED("blocked_by_you"), NONE("none");

        private final String value;

        Incoming(final String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value;
        }

        public static Incoming fromString(String text) {
            if (text != null) {
                for (Incoming b : Incoming.values()) {
                    if (text.equalsIgnoreCase(b.value)) {
                        return b;
                    }
                }
            }
            throw new IllegalArgumentException("No constant with text " + text + " found");
        }
    }

    private Outgoing outgoing;
    private Incoming incoming;
    private boolean targetUserIsPrivate;
    private Date createdAt;

    /**
     * Constructor for UserRelationship.
     */
    public UserRelationship() {
        createdAt = new Date();
    }

    /**
     * Getter for the field <code>outgoing</code>.
     *
     * @return a {@link io.reneses.tela.modules.instagram.models.UserRelationship.Outgoing} object.
     */
    public Outgoing getOutgoing() {
        return outgoing;
    }

    /**
     * Setter for the field <code>outgoing</code>.
     *
     * @param outgoingRelationship a {@link io.reneses.tela.modules.instagram.models.UserRelationship.Outgoing} object.
     */
    public void setOutgoing(Outgoing outgoingRelationship) {
        this.outgoing = outgoingRelationship;
    }

    /**
     * Setter for the field <code>outgoing</code>.
     *
     * @param outgoingRelationship a {@link java.lang.String} object.
     */
    @JsonProperty("outgoing_status")
    public void setOutgoing(String outgoingRelationship) {
        this.outgoing = Outgoing.fromString(outgoingRelationship);
    }

    /**
     * Getter for the field <code>incoming</code>.
     *
     * @return a {@link io.reneses.tela.modules.instagram.models.UserRelationship.Incoming} object.
     */
    public Incoming getIncoming() {
        return incoming;
    }

    /**
     * Setter for the field <code>incoming</code>.
     *
     * @param incomingRelationship a {@link io.reneses.tela.modules.instagram.models.UserRelationship.Incoming} object.
     */
    public void setIncoming(Incoming incomingRelationship) {
        this.incoming = incomingRelationship;
    }

    /**
     * Setter for the field <code>incoming</code>.
     *
     * @param incomingRelationship a {@link java.lang.String} object.
     */
    @JsonProperty("incoming_status")
    public void setIncoming(String incomingRelationship) {
        this.incoming = Incoming.fromString(incomingRelationship);
    }

    /**
     * isTargetUserIsPrivate.
     *
     * @return a boolean.
     */
    public boolean isTargetUserIsPrivate() {
        return targetUserIsPrivate;
    }

    /**
     * Setter for the field <code>targetUserIsPrivate</code>.
     *
     * @param targetUserIsPrivate a boolean.
     */
    @JsonProperty("target_user_is_private")
    public void setTargetUserIsPrivate(boolean targetUserIsPrivate) {
        this.targetUserIsPrivate = targetUserIsPrivate;
    }

    /**
     * Getter for the field <code>createdAt</code>.
     *
     * @return a {@link java.util.Date} object.
     */
    public Date getCreatedAt() {
        return createdAt;
    }

    /**
     * Setter for the field <code>createdAt</code>.
     *
     * @param createdAt a {@link java.util.Date} object.
     */
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "UserRelationship{" +
                "outgoing=" + outgoing +
                ", incoming=" + incoming +
                ", targetUserIsPrivate=" + targetUserIsPrivate +
                ", createdAt=" + createdAt +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserRelationship that = (UserRelationship) o;

        if (targetUserIsPrivate != that.targetUserIsPrivate) return false;
        if (outgoing != that.outgoing) return false;
        return incoming == that.incoming;

    }

    @Override
    public int hashCode() {
        int result = outgoing.hashCode();
        result = 31 * result + incoming.hashCode();
        result = 31 * result + (targetUserIsPrivate ? 1 : 0);
        return result;
    }
}
