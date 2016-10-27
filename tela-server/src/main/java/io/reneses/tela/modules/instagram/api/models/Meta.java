package io.reneses.tela.modules.instagram.api.models;


import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Meta class.
 */
public class Meta {

    @JsonProperty("error_type")
    private String errorType;

    private int code;

    @JsonProperty("error_message")
    private String errorMessage;

    /**
     * Getter for the field <code>errorType</code>.
     *
     * @return a {@link java.lang.String} object.
     */
    public String getErrorType() {
        return errorType;
    }

    /**
     * Setter for the field <code>errorType</code>.
     *
     * @param errorType a {@link java.lang.String} object.
     */
    public void setErrorType(String errorType) {
        this.errorType = errorType;
    }

    /**
     * Getter for the field <code>code</code>.
     *
     * @return a int.
     */
    public int getCode() {
        return code;
    }

    /**
     * Setter for the field <code>code</code>.
     *
     * @param code a int.
     */
    public void setCode(int code) {
        this.code = code;
    }

    /**
     * Getter for the field <code>errorMessage</code>.
     *
     * @return a {@link java.lang.String} object.
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * Setter for the field <code>errorMessage</code>.
     *
     * @param errorMessage a {@link java.lang.String} object.
     */
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    /**
     * isError.
     *
     * @return a boolean.
     */
    public boolean isError() {
        return this.code >= 400 && this.code < 600;
    }

    /**
     * isRestrictedResource.
     *
     * @return a boolean.
     */
    public boolean isRestrictedResource() {
        return this.errorType != null && this.errorType.equalsIgnoreCase("APINotAllowedError");
    }

    /**
     * isNotExistingResource.
     *
     * @return a boolean.
     */
    public boolean isNotExistingResource() {
        return this.errorType != null && this.errorType.equalsIgnoreCase("APINotFoundError");
    }

    /**
     * isAccessTokenException.
     *
     * @return a boolean.
     */
    public boolean isAccessTokenException() {
        return this.errorType != null && this.errorType.equalsIgnoreCase("OAuthAccessTokenException");
    }

    /**
     * isInvalidAccessToken.
     *
     * @return a boolean.
     */
    public boolean isInvalidAccessToken() {
        return isAccessTokenException() && this.errorMessage.contains("invalid");
    }

}
