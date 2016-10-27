package io.reneses.tela.core.dispatcher.exceptions;

import io.reneses.tela.core.api.exceptions.ApiException;

import java.util.Arrays;

/**
 * ModuleNotDefinedException class.
 */
public class ModuleNotDefinedException extends ApiException {

    /**
     * Constructor for ModuleNotDefinedException.
     */
    public ModuleNotDefinedException() {
        super("The module requested is not defined", 404);
    }

    /**
     * Constructor for ModuleNotDefinedException.
     *
     * @param moduleName Module name
     */
    public ModuleNotDefinedException(String moduleName) {
        super("The module '" + moduleName + "' is not defined", 404);
    }

}
