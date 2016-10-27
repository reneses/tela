package io.reneses.tela.core.api.exceptions;


/**
 * API raised an exception due to a parameter presents some problem
 */
public class RequiredParameterException extends ParameterException {

    /**
     * Constructor for RequiredParameterException.
     *
     * @param message Error message
     */
    public RequiredParameterException(String message) {
        super(message);
    }

}
