package io.reneses.tela.core.api.exceptions;


/**
 * Exception raised by the API, accepting a HTTP status code
 */
public abstract class ApiException extends Exception {

    private int statusCode;

    /**
     * API Exception constructor
     *
     * @param message    Exception message
     * @param statusCode HTTP code
     */
    public ApiException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    /**
     * Get the HTTP status code
     *
     * @return HTTP status code
     */
    public int getStatusCode() {
        return statusCode;
    }

}
