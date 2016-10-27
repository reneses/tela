package io.reneses.tela.core.api.controllers;


import io.reneses.tela.core.api.exceptions.ApiException;
import io.reneses.tela.core.sessions.models.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Map;

/**
 * Controller in charge of executing actions
 */
@Path("/action")
public class ActionController extends TelaController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ActionController.class);

    /**
     * Constructor for ActionController.
     */
    public ActionController() {
        super();
    }

    /**
     * Execute an action
     *
     * @param request       Http request
     * @param authorization Authorization header
     * @param module        Module
     * @param action        Action
     * @return Action result
     */
    @GET
    @Path("/{module}/{action}/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response execute(
            @Context HttpServletRequest request,
            @HeaderParam("Authorization") String authorization,
            @PathParam("module") String module,
            @PathParam("action") String action) {

        Session session = null;
        try {

            session = extractSession(authorization);
            Map<String, String[]> params = request.getParameterMap();
            Object result = dispatcher.dispatch(session, module, action, params);
            LOGGER.info("{} ({}) - [Action] {}/{}", request.getRemoteAddr(), session, module, action);
            return buildResponse(result);

        } catch (ApiException e) {
            LOGGER.error("{} ({}) - [Action] {}/{} - API exception: {}", request.getRemoteAddr(), session, module, action, e.getMessage());
            return buildErrorResponse(e.getStatusCode(), e.getMessage());
        } catch (Exception e) {
            LOGGER.error(request.getRemoteAddr() + ":" + session + " - [Action] " + module + "/" + action + " - Unknown server error", e);
            return buildErrorResponse(500, e.getMessage());
        }
    }

}
