package io.reneses.tela.modules.instagram.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.*;

/**
 * Media class.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Media {

    /** Constant <code>VIDEO="video"</code> */
    public static final String VIDEO = "video";
    /** Constant <code>IMAGE="image"</code> */
    public static final String IMAGE = "image";

    private String id;
    private Comment caption;
    private String link, type, filter;
    private User user;
    private Set<String> tags = new HashSet<>();
    private Location location;
    private Map<String, MediaResource> images = new HashMap<>(), videos = new HashMap<>();
    private List<Comment> comments = new ArrayList<>();

    @JsonIgnore
    private int numberOfComments;

    private List<User> likes = new ArrayList<>();

    @JsonIgnore
    private int numberOfLikes;

    private Date createdTime;

    @JsonProperty("users_in_photo")
    private List<TaggedUser> taggedUsers = new ArrayList<>();

    @JsonProperty("user_has_liked")
    private Boolean userHasLiked;

    /**
     * Getter for the field <code>numberOfLikes</code>.
     *
     * @return a int.
     */
    public int getNumberOfLikes() {
        return numberOfLikes;
    }

    /**
     * Setter for the field <code>likes</code>.
     *
     * @param likes a {@link java.util.List} object.
     */
    public void setLikes(List<User> likes) {
        this.likes = likes;
    }

    /**
     * Getter for the field <code>likes</code>.
     *
     * @return a {@link java.util.List} object.
     */
    public List<User> getLikes() {
        return likes;
    }

    /**
     * Setter for the field <code>likes</code>.
     *
     * @param value a {@link java.util.Map} object.
     */
    @JsonProperty("likes")
    public void setLikes(Map<String, Object> value) {
        this.numberOfLikes = (int) value.get("count");
        this.likes = (List<User>) value.get("data");
        if (this.likes == null)
            this.likes = new ArrayList<>();
    }

    /**
     * getJsonLikes.
     *
     * @return a {@link java.util.Map} object.
     */
    @JsonProperty("likes")
    public Map<String, Object> getJsonLikes() {
        Map<String, Object> json = new HashMap<>();
        json.put("count", this.likes.size());
        json.put("data", this.likes);
        return json;
    }

    /**
     * Setter for the field <code>comments</code>.
     *
     * @param comments a {@link java.util.List} object.
     */
    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    /**
     * Getter for the field <code>comments</code>.
     *
     * @return a {@link java.util.List} object.
     */
    public List<Comment> getComments() {
        return comments;
    }

    /**
     * Setter for the field <code>comments</code>.
     *
     * @param value a {@link java.util.Map} object.
     */
    @JsonProperty("comments")
    public void setComments(Map<String, Object> value) {
        this.numberOfComments = (int) value.get("count");
        this.comments = (List<Comment>) value.get("data");
        if (this.comments == null)
            this.comments = new ArrayList<>();
    }

    /**
     * getJsonComments.
     *
     * @return a {@link java.util.Map} object.
     */
    @JsonProperty("comments")
    public Map<String, Object> getJsonComments() {
        Map<String, Object> json = new HashMap<>();
        json.put("count", this.comments.size());
        json.put("data", this.comments);
        return json;
    }

    /**
     * Setter for the field <code>numberOfComments</code>.
     *
     * @param numberOfComments a int object.
     */
    public void setNumberOfComments(int numberOfComments) {
        this.numberOfComments = numberOfComments;
    }

    /**
     * Setter for the field <code>numberOfLikes</code>.
     *
     * @param numberOfLikes a int object.
     */
    public void setNumberOfLikes(int numberOfLikes) {
        this.numberOfLikes = numberOfLikes;
    }

    /**
     * Getter for the field <code>numberOfComments</code>.
     *
     * @return a int.
     */
    public int getNumberOfComments() {
        return numberOfComments;
    }

    /**
     * Getter for the field <code>createdTime</code>.
     *
     * @return a {@link java.util.Date} object.
     */
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
     * @param createdTime a {@link java.util.Date} object.
     */
    @JsonProperty("created_time")
    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    /**
     * Getter for the field <code>userHasLiked</code>.
     *
     * @return a {@link java.lang.Boolean} object.
     */
    public Boolean getUserHasLiked() {
        return userHasLiked;
    }

    /**
     * Setter for the field <code>userHasLiked</code>.
     *
     * @param userHasLiked a {@link java.lang.Boolean} object.
     */
    public void setUserHasLiked(Boolean userHasLiked) {
        this.userHasLiked = userHasLiked;
    }

    /**
     * isVideo.
     *
     * @return a boolean.
     */
    public boolean isVideo() {
        return this.type.equals(VIDEO);
    }

    /**
     * isImage.
     *
     * @return a boolean.
     */
    public boolean isImage() {
        return this.type.equals(IMAGE);
    }

    /**
     * Getter for the field <code>type</code>.
     *
     * @return a {@link java.lang.String} object.
     */
    public String getType() {
        return type;
    }

    /**
     * Setter for the field <code>id</code>.
     *
     * @param id a {@link java.lang.String} object.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Getter for the field <code>id</code>.
     *
     * @return a {@link java.lang.String} object.
     */
    public String getId() {
        return id;
    }

    /**
     * Getter for the field <code>caption</code>.
     *
     * @return a {@link io.reneses.tela.modules.instagram.models.Comment} object.
     */
    public Comment getCaption() {
        return caption;
    }

    /**
     * Setter for the field <code>caption</code>.
     *
     * @param caption a {@link io.reneses.tela.modules.instagram.models.Comment} object.
     */
    public void setCaption(Comment caption) {
        this.caption = caption;
    }

    /**
     * Getter for the field <code>link</code>.
     *
     * @return a {@link java.lang.String} object.
     */
    public String getLink() {
        return link;
    }

    /**
     * Setter for the field <code>link</code>.
     *
     * @param link a {@link java.lang.String} object.
     */
    public void setLink(String link) {
        this.link = link;
    }

    /**
     * Setter for the field <code>type</code>.
     *
     * @param type a {@link java.lang.String} object.
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Getter for the field <code>filter</code>.
     *
     * @return a {@link java.lang.String} object.
     */
    public String getFilter() {
        return filter;
    }

    /**
     * Setter for the field <code>filter</code>.
     *
     * @param filter a {@link java.lang.String} object.
     */
    public void setFilter(String filter) {
        this.filter = filter;
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
     * Getter for the field <code>tags</code>.
     *
     * @return a {@link java.util.Set} object.
     */
    public Set<String> getTags() {
        return tags;
    }

    /**
     * Setter for the field <code>tags</code>.
     *
     * @param tags a {@link java.util.Set} object.
     */
    public void setTags(Set<String> tags) {
        this.tags = tags;
    }

    /**
     * Getter for the field <code>location</code>.
     *
     * @return a {@link io.reneses.tela.modules.instagram.models.Location} object.
     */
    public Location getLocation() {
        return location;
    }

    /**
     * Setter for the field <code>location</code>.
     *
     * @param location a {@link io.reneses.tela.modules.instagram.models.Location} object.
     */
    public void setLocation(Location location) {
        this.location = location;
    }

    /**
     * Getter for the field <code>images</code>.
     *
     * @return a {@link java.util.Map} object.
     */
    public Map<String, MediaResource> getImages() {
        return images;
    }

    /**
     * Setter for the field <code>images</code>.
     *
     * @param images a {@link java.util.Map} object.
     */
    public void setImages(Map<String, MediaResource> images) {
        this.images = images;
        for (Map.Entry<String, MediaResource> image : images.entrySet()) {
            image.getValue().setCode(image.getKey());
        }
    }

    /**
     * Getter for the field <code>videos</code>.
     *
     * @return a {@link java.util.Map} object.
     */
    public Map<String, MediaResource> getVideos() {
        return videos;
    }

    /**
     * Setter for the field <code>videos</code>.
     *
     * @param videos a {@link java.util.Map} object.
     */
    public void setVideos(Map<String, MediaResource> videos) {
        this.videos = videos;
        for (Map.Entry<String, MediaResource> video : videos.entrySet()) {
            video.getValue().setCode(video.getKey());
        }
    }

    /**
     * Getter for the field <code>taggedUsers</code>.
     *
     * @return a {@link java.util.List} object.
     */
    public List<TaggedUser> getTaggedUsers() {
        return taggedUsers;
    }

    /**
     * Setter for the field <code>taggedUsers</code>.
     *
     * @param taggedUsers a {@link java.util.List} object.
     */
    public void setTaggedUsers(List<TaggedUser> taggedUsers) {
        this.taggedUsers = taggedUsers == null? new ArrayList<>() : taggedUsers;
    }

    @Override
    public String toString() {
        return "Media{" +
                "id='" + id + '\'' +
                ", caption=" + caption +
                ", link='" + link + '\'' +
                ", type='" + type + '\'' +
                ", filter='" + filter + '\'' +
                ", user=" + user +
                ", tags=" + tags +
                ", location=" + location +
                ", images=" + images +
                ", videos=" + videos +
                ", comments=" + comments +
                ", numberOfComments=" + numberOfComments +
                ", likes=" + likes +
                ", numberOfLikes=" + numberOfLikes +
                ", createdTime=" + createdTime +
                ", taggedUsers=" + taggedUsers +
                ", userHasLiked=" + userHasLiked +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Media media = (Media) o;

        if (numberOfComments != media.numberOfComments) return false;
        if (numberOfLikes != media.numberOfLikes) return false;
        if (!id.equals(media.id)) return false;
        if (caption != null ? !caption.equals(media.caption) : media.caption != null) return false;
        if (!link.equals(media.link)) return false;
        if (!type.equals(media.type)) return false;
        if (!filter.equals(media.filter)) return false;
        if (user.getId() != media.user.getId()) return false;
        if (tags != null ? !tags.equals(media.tags) : media.tags != null) return false;
        if (location != null ? !location.equals(media.location) : media.location != null) return false;
        if (images != null ? !images.equals(media.images) : media.images != null) return false;
        if (videos != null ? !videos.equals(media.videos) : media.videos != null) return false;
        if (comments != null ? !comments.equals(media.comments) : media.comments != null) return false;
        if (likes != null ? !likes.equals(media.likes) : media.likes != null) return false;
        if (createdTime != null ? !createdTime.equals(media.createdTime) : media.createdTime != null) return false;
        if (taggedUsers != null ? !taggedUsers.equals(media.taggedUsers) : media.taggedUsers != null) return false;
        return userHasLiked != null ? userHasLiked.equals(media.userHasLiked) : media.userHasLiked == null;

    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + (caption != null ? caption.hashCode() : 0);
        result = 31 * result + link.hashCode();
        result = 31 * result + type.hashCode();
        result = 31 * result + filter.hashCode();
        result = 31 * result + Long.hashCode(user.getId());
        result = 31 * result + (tags != null ? tags.hashCode() : 0);
        result = 31 * result + (location != null ? location.hashCode() : 0);
        result = 31 * result + (images != null ? images.hashCode() : 0);
        result = 31 * result + (videos != null ? videos.hashCode() : 0);
        result = 31 * result + (comments != null ? comments.hashCode() : 0);
        result = 31 * result + numberOfComments;
        result = 31 * result + (likes != null ? likes.hashCode() : 0);
        result = 31 * result + numberOfLikes;
        result = 31 * result + (createdTime != null ? createdTime.hashCode() : 0);
        result = 31 * result + (taggedUsers != null ? taggedUsers.hashCode() : 0);
        result = 31 * result + (userHasLiked != null ? userHasLiked.hashCode() : 0);
        return result;
    }
}
