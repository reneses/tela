package io.reneses.tela.modules.instagram.api.responses;


import com.fasterxml.jackson.annotation.JsonProperty;
import io.reneses.tela.modules.instagram.api.models.Meta;

/**
 * Abstract EnvelopeResponse class.
 */
public abstract class EnvelopeResponse {

    @JsonProperty("meta")
    private Meta meta;

    /**
     * Getter for the field <code>meta</code>.
     *
     * @return a {@link io.reneses.tela.modules.instagram.api.models.Meta} object.
     */
    public Meta getMeta() {
        return meta;
    }

    /**
     * Setter for the field <code>meta</code>.
     *
     * @param meta a {@link io.reneses.tela.modules.instagram.api.models.Meta} object.
     */
    public void setMeta(Meta meta) {
        this.meta = meta;
    }



}
