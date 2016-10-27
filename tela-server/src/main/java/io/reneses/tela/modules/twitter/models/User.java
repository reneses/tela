package io.reneses.tela.modules.twitter.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * User data class representing a Twitter user
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {

    private long id;
    private String name, url;

    @JsonProperty("profile_image_url")
    private String picture;

    @JsonProperty("screen_name")
    private String screenName;

    @JsonProperty("followers_count")
    private int numberOfFollowers;

    @JsonProperty("friends_count")
    private int numberOfFollowing;

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
     * Getter for the field <code>url</code>.
     *
     * @return a {@link java.lang.String} object.
     */
    public String getUrl() {
        return url;
    }

    /**
     * Setter for the field <code>url</code>.
     *
     * @param url a {@link java.lang.String} object.
     */
    public void setUrl(String url) {
        this.url = url;
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
     * Getter for the field <code>screenName</code>.
     *
     * @return a {@link java.lang.String} object.
     */
    public String getScreenName() {
        return screenName;
    }

    /**
     * Setter for the field <code>screenName</code>.
     *
     * @param screenName a {@link java.lang.String} object.
     */
    public void setScreenName(String screenName) {
        this.screenName = screenName;
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
     * @param numberOfFollowing a int.
     */
    public void setNumberOfFollowing(int numberOfFollowing) {
        this.numberOfFollowing = numberOfFollowing;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (id != user.id) return false;
        if (numberOfFollowers != user.numberOfFollowers) return false;
        if (numberOfFollowing != user.numberOfFollowing) return false;
        if (name != null ? !name.equals(user.name) : user.name != null) return false;
        if (url != null ? !url.equals(user.url) : user.url != null) return false;
        if (!picture.equals(user.picture)) return false;
        return screenName.equals(user.screenName);

    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (url != null ? url.hashCode() : 0);
        result = 31 * result + picture.hashCode();
        result = 31 * result + screenName.hashCode();
        result = 31 * result + numberOfFollowers;
        result = 31 * result + numberOfFollowing;
        return result;
    }
}
