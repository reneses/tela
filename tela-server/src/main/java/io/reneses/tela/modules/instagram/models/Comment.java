package io.reneses.tela.modules.instagram.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

/**
 * Comment class.
 */
public class Comment {

    private long id;

    @JsonProperty("created_time")
    private Date createdTime;

    private String text;

    @JsonProperty("from")
    private User user;

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
     * Getter for the field <code>createdTime</code>.
     *
     * @return a {@link java.util.Date} object.
     */
    @JsonIgnore
    public Date getCreatedTime() {
        return createdTime;
    }

    /**
     * getJsonCreatedTime.
     *
     * @return a long.
     */
    @JsonProperty("created_time")
    public long getJsonCreatedTime() {
        return createdTime.getTime();
    }

    /**
     * Setter for the field <code>createdTime</code>.
     *
     * @param createdDate a {@link java.util.Date} object.
     */
    public void setCreatedTime(Date createdDate) {
        this.createdTime = createdDate;
    }

    /**
     * Getter for the field <code>text</code>.
     *
     * @return a {@link java.lang.String} object.
     */
    public String getText() {
        return text;
    }

    /**
     * Setter for the field <code>text</code>.
     *
     * @param text a {@link java.lang.String} object.
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * Getter for the field <code>user</code>.
     *
     * @return a {@link io.reneses.tela.modules.instagram.models.User} object.
     */
    public User getUser() {
        return user;
    }

    /**
     * Setter for the field <code>user</code>.
     *
     * @param user a {@link io.reneses.tela.modules.instagram.models.User} object.
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Comment comment = (Comment) o;

        if (id != comment.id) return false;
        if (!createdTime.equals(comment.createdTime)) return false;
        if (!text.equals(comment.text)) return false;
        return user.getId() == comment.user.getId();

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + createdTime.hashCode();
        result = 31 * result + text.hashCode();
        result = 31 * result + Long.hashCode(user.getId());
        return result;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", createdTime=" + createdTime +
                ", text='" + text + '\'' +
                ", user=" + user +
                '}';
    }
}
