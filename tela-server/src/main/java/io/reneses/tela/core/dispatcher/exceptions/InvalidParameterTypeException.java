package io.reneses.tela.core.dispatcher.exceptions;

import io.reneses.tela.core.api.exceptions.ApiException;


/**
 * InvalidParameterTypeException class.
 */
public class InvalidParameterTypeException extends ApiException {

    /**
     * Constructor for InvalidParameterTypeException.
     *
     * @param paramValue Value of the parameter
     */
    public InvalidParameterTypeException(Object paramValue) {
        super("The parameter value '" + paramValue + "' could not be casted to a valid type'", 422);
    }

}
