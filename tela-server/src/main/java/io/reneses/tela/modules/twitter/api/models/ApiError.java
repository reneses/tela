package io.reneses.tela.modules.twitter.api.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Error model
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class ApiError {

    private String message;
    private int code;

    /**
     * Constructor
     *
     * @param code Code
     * @param message Message
     */
    public ApiError(int code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * Default constructor needed for deserialziation
     */
    public ApiError() {
    }

    /**
     * Message getter
     *
     * @return Message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Message setter
     *
     * @param message Message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Get error code
     *
     * @return Code
     */
    public int getCode() {
        return code;
    }

    /**
     * Set error code
     *
     * @param code Code
     */
    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "ApiError{" +
                "message='" + message + '\'' +
                ", code=" + code +
                '}';
    }
}
