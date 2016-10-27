package io.reneses.tela.core.api.exceptions;


/**
 * API raised an exception due to a required parameter that is missing
 */
public class ParameterException extends ApiException {

    /**
     * Constructor for ParameterException.
     *
     * @param message Error message
     */
    public ParameterException(String message) {
        super(message, 422);
    }

}
