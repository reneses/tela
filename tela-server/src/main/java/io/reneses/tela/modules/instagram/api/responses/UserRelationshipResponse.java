package io.reneses.tela.modules.instagram.api.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.reneses.tela.modules.instagram.models.UserRelationship;

/**
 * UserRelationshipResponse class.
 */
public class UserRelationshipResponse extends EnvelopeResponse {

    @JsonProperty("data")
    private UserRelationship relationship;

    /**
     * Setter for the field <code>relationship</code>.
     *
     * @param relationship a {@link UserRelationship} object.
     */
    public void setRelationship(UserRelationship relationship) {
        this.relationship = relationship;
    }

    /**
     * Getter for the field <code>relationship</code>.
     *
     * @return a {@link UserRelationship} object.
     */
    public UserRelationship getRelationship() {
        return relationship;
    }

}
