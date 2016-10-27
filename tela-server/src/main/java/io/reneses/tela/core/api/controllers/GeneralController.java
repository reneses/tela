package io.reneses.tela.core.api.controllers;


import io.reneses.tela.core.dispatcher.exceptions.ModuleNotDefinedException;
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
            return buildErrorResponse(404, "The module '" + module + "' is not configured");
        }
    }

}
