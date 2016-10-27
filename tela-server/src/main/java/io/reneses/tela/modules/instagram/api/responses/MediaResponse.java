package io.reneses.tela.modules.instagram.api.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.reneses.tela.modules.instagram.api.models.Pagination;
import io.reneses.tela.modules.instagram.models.Media;

import java.util.List;

/**
 * MediaResponse class.
 */
public class MediaResponse extends EnvelopeResponse {

    @JsonProperty("data")
    private List<Media> media;

    @JsonProperty("pagination")
    private Pagination pagination;

    /**
     * Getter for the field <code>media</code>.
     *
     * @return a {@link java.util.List} object.
     */
    public List<Media> getMedia() {
        return media;
    }

    /**
     * Setter for the field <code>media</code>.
     *
     * @param media a {@link java.util.List} object.
     */
    public void setMedia(List<Media> media) {
        this.media = media;
    }

    /**
     * Getter for the field <code>pagination</code>.
     *
     * @return a {@link Pagination} object.
     */
    public Pagination getPagination() {
        return pagination;
    }

    /**
     * Setter for the field <code>pagination</code>.
     *
     * @param pagination a {@link Pagination} object.
     */
    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }

}
