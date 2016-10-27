package io.reneses.tela.modules.instagram.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

/**
 * Counts class.
 */
public class Counts {

    @JsonProperty("media")
    private int numberOfMedia;

    @JsonProperty("followed_by")
    private int numberOfFollowers;

    @JsonProperty("follows")
    private int numberOfFollowing;

    @JsonProperty("created_at")
    private Date createdAt;

    /**
     * Constructor for Counts.
     */
    public Counts() {
        this.createdAt = new Date();
    }
    /**
     * Constructor for Counts.
     *
     * @param numberOfMedia a int.
     * @param numberOfFollowers a int.
     * @param numberOfFollowing a int.
     */
    public Counts(int numberOfMedia, int numberOfFollowers, int numberOfFollowing) {
        this.numberOfMedia = numberOfMedia;
        this.numberOfFollowers = numberOfFollowers;
        this.numberOfFollowing = numberOfFollowing;
        this.createdAt = new Date();
    }

    /**
     * Getter for the field <code>numberOfMedia</code>.
     *
     * @return a int.
     */
    public int getNumberOfMedia() {
        return numberOfMedia;
    }

    /**
     * Setter for the field <code>numberOfMedia</code>.
     *
     * @param numberOfMedia a int.
     */
    public void setNumberOfMedia(int numberOfMedia) {
        this.numberOfMedia = numberOfMedia;
    }

    /**
     * Getter for the field <code>numberOfFollowers</code>.
     *
     * @return a int.
     */
    public int getNumberOfFollowers() {
        return numberOfFollowers;
    }

    /**
     * Setter for the field <code>numberOfFollowers</code>.
     *
     * @param numberOfFollowers a int.
     */
    public void setNumberOfFollowers(int numberOfFollowers) {
        this.numberOfFollowers = numberOfFollowers;
    }

    /**
     * Getter for the field <code>numberOfFollowing</code>.
     *
     * @return a int.
     */
    public int getNumberOfFollowing() {
        return numberOfFollowing;
    }

    /**
     * Setter for the field <code>numberOfFollowing</code>.
     *
     * @param numberOfFollows a int.
     */
    public void setNumberOfFollowing(int numberOfFollows) {
        this.numberOfFollowing = numberOfFollows;
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
        if (createdAt != null)
            this.createdAt = createdAt;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return "Counts{" +
                "numberOfMedia=" + numberOfMedia +
                ", numberOfFollowers=" + numberOfFollowers +
                ", numberOfFollows=" + numberOfFollowing +
                '}';
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Counts)) return false;

        Counts counts = (Counts) o;

        if (numberOfMedia != counts.numberOfMedia) return false;
        if (numberOfFollowers != counts.numberOfFollowers) return false;
        return numberOfFollowing == counts.numberOfFollowing;

    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        int result = numberOfMedia;
        result = 31 * result + numberOfFollowers;
        result = 31 * result + numberOfFollowing;
        return result;
    }
}
