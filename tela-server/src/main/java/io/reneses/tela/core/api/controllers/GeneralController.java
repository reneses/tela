package io.reneses.tela.core.api.controllers;


import io.reneses.tela.core.dispatcher.exceptions.ModuleNotDefinedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * General controller
 */
@Path("/")
public class GeneralController extends TelaController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ActionController.class);

    /**
     * Constructor for GeneralController.
     */
    public GeneralController() {
        super();
    }

    /**
     * Simply act as test endpoint
     *
     * @return OK
     */
    @GET
    @Path("/test")
    @Produces(MediaType.TEXT_PLAIN)
    public String test() {
        return "OK";
    }

    /**
     * Help endpoint
     *
     * @return A list of all the configured modules and its actions
     */
    @GET
    @Path("/help")
    @Produces(MediaType.APPLICATION_JSON)
    public Response help() {
        return buildResponse(dispatcher.getHelp());
    }

    /**
     * Help of a specific module
     *
     * @param module Module
     * @return List of actions of the module
     */
    @GET
    @Path("/help/{module}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response help(@PathParam("module") String module) {
        try {
            return buildResponse(dispatcher.getHelp(module));
        }
        catch (ModuleNotDefinedException e) {
            LOGGER.error(String.format("[General] Help - Module '%s' not defined", module));
            return buildErrorResponse(404, "The module '" + module + "' is not configured");
        }
    }

}
