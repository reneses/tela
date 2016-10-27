package io.reneses.tela.modules.instagram.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * User class.
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class User {

    // Baisc user: username, picture, id and full name

    private String username, bio, website;

    @JsonProperty("id")
    private long id;

    @JsonProperty("profile_picture")
    private String picture;

    @JsonProperty("full_name")
    private String fullName;

    @JsonProperty("counts")
    private Counts counts;

    /**
     * Getter for the field <code>username</code>.
     *
     * @return a {@link java.lang.String} object.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Setter for the field <code>username</code>.
     *
     * @param username a {@link java.lang.String} object.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Getter for the field <code>bio</code>.
     *
     * @return a {@link java.lang.String} object.
     */
    public String getBio() {
        return bio;
    }

    /**
     * Setter for the field <code>bio</code>.
     *
     * @param bio a {@link java.lang.String} object.
     */
    public void setBio(String bio) {
        this.bio = bio;
    }

    /**
     * Getter for the field <code>website</code>.
     *
     * @return a {@link java.lang.String} object.
     */
    public String getWebsite() {
        return website;
    }

    /**
     * Setter for the field <code>website</code>.
     *
     * @param website a {@link java.lang.String} object.
     */
    public void setWebsite(String website) {
        this.website = website;
    }

    /**
     * Getter for the field <code>picture</code>.
     *
     * @return a {@link java.lang.String} object.
     */
    public String getPicture() {
        return picture;
    }

    /**
     * Setter for the field <code>picture</code>.
     *
     * @param picture a {@link java.lang.String} object.
     */
    public void setPicture(String picture) {
        this.picture = picture;
    }

    /**
     * Getter for the field <code>fullName</code>.
     *
     * @return a {@link java.lang.String} object.
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * Setter for the field <code>fullName</code>.
     *
     * @param fullName a {@link java.lang.String} object.
     */
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    /**
     * Getter for the field <code>id</code>.
     *
     * @return a long.
     */
    public long getId() {
        return id;
    }

    /**
     * Setter for the field <code>id</code>.
     *
     * @param id a long.
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Getter for the field <code>counts</code>.
     *
     * @return a {@link io.reneses.tela.modules.instagram.models.Counts} object.
     */
    public Counts getCounts() {
        return counts;
    }

    /**
     * Setter for the field <code>counts</code>.
     *
     * @param counts a {@link io.reneses.tela.modules.instagram.models.Counts} object.
     */
    public void setCounts(Counts counts) {
        this.counts = counts;
    }


    /** {@inheritDoc} */
    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", bio='" + bio + '\'' +
                ", website='" + website + '\'' +
                ", id='" + id + '\'' +
                ", picture='" + picture + '\'' +
                ", fullName='" + fullName + '\'' +
                ", counts=" + counts +
                '}';
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;

        User user = (User) o;

        return id == user.id;

    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }
}
