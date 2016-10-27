package io.reneses.tela.core.api.controllers;

import io.reneses.tela.core.api.exceptions.ApiException;
import io.reneses.tela.core.api.exceptions.RequiredParameterException;
import io.reneses.tela.core.api.responses.ScheduledActionResponse;
import io.reneses.tela.core.scheduler.models.ScheduledAction;
import io.reneses.tela.core.sessions.models.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Scheduler controller exposing the scheduling functionality
 */
@Path("/schedule")
public class SchedulerController extends TelaController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SchedulerController.class);

    /**
     * Constructor for SchedulerController.
     */
    public SchedulerController() {
        super();
    }

    /**
     * Schedule an action
     *
     * @param request       http request
     * @param authorization authorization header
     * @param module        Action module
     * @param action        Action
     * @return Scheduled task
     */
    @GET
    @Path("/{module}/{action}/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response schedule(
            @Context HttpServletRequest request,
            @HeaderParam("Authorization") String authorization,
            @PathParam("module") String module,
            @PathParam("action") String action) {

        Map<String, String[]> params = new HashMap<>(request.getParameterMap());
        Session session = null;
        int delay = -1;
        try {

            if (module == null || module.isEmpty() || action == null || action.isEmpty())
                throw new RequiredParameterException("Authorization parameters invalid");

            session = extractSession(authorization);
            delay = getIntParameter(params, "delay", -1);
            params.remove("delay");
            ScheduledAction scheduledAction = new ScheduledAction(session.getAccessToken(), delay, module, action, params);
            Object result = scheduler.schedule(scheduledAction);
            ScheduledActionResponse<Object> jsonResponse = new ScheduledActionResponse<>(scheduledAction, result);
            LOGGER.info("{}:{} [Schedule] {}/{} each {}s", request.getRemoteAddr(), session, module, action, scheduledAction.getDelay());
            return buildResponse(jsonResponse);

        } catch (ApiException e) {
            LOGGER.error("{}:{} [Schedule] {}/{} each {}s - {}", request.getRemoteAddr(), session, module, action, delay, e.getMessage());
            return buildErrorResponse(e.getStatusCode(), e.getMessage());
        } catch (Exception e) {
            LOGGER.error(request.getRemoteAddr() + ":" + session + " [Schedule] " + module + "/" + action + " each " + delay + "s - Unknown server error", e);
            return buildErrorResponse(500, e.getMessage());
        }
    }

    /**
     * Get all the scheduled actions by the current session
     *
     * @param request       Http request
     * @param authorization Authorization header
     * @return List of scheduled actions
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getScheduled(
            @Context HttpServletRequest request,
            @HeaderParam("Authorization") String authorization) {

        Session session = null;
        try {

            session = extractSession(authorization);
            LOGGER.info("{}:{} [Schedule] Get all scheduled", request.getRemoteAddr(), session);
            List<ScheduledAction> tasks = scheduler.getActions(session);
            return buildResponse(tasks);

        } catch (ApiException e) {
            LOGGER.error("{}:{} [Schedule] Get all scheduled - {}", request.getRemoteAddr(), session, e.getMessage());
            return buildErrorResponse(e.getStatusCode(), e.getMessage());
        } catch (Exception e) {
            LOGGER.error(request.getRemoteAddr() + ":" + session + " [Schedule] Get all scheduled - Unknown server error", e);
            return buildErrorResponse(500, e.getMessage());
        }
    }

    /**
     * Cancel all the scheduled actions by the session
     *
     * @param request       Http request
     * @param authorization Authorization header
     * @return Success response
     */
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response cancelAll(
            @Context HttpServletRequest request,
            @HeaderParam("Authorization") String authorization) {

        Session session = null;
        try {

            session = extractSession(authorization);
            LOGGER.info("{}:{} [Schedule] Cancel all scheduled", request.getRemoteAddr(), session);
            scheduler.cancelAll(session);
            return buildResponse("Success");

        } catch (ApiException e) {
            LOGGER.error("{}:{} [Schedule] Cancel all scheduled - {}", request.getRemoteAddr(), session, e.getMessage());
            return buildErrorResponse(e.getStatusCode(), e.getMessage());
        } catch (Exception e) {
            LOGGER.error(request.getRemoteAddr() + ":" + session + " [Schedule] Cancel all scheduled - Unknown server error", e);
            return buildErrorResponse(500, e.getMessage());
        }
    }

    /**
     * Delete a specific scheduled action
     *
     * @param request       Http request
     * @param authorization Authorization header
     * @param actionId      ID of the action to remove
     * @return Success response
     */
    @DELETE
    @Path("/{actionId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response cancel(
            @Context HttpServletRequest request,
            @HeaderParam("Authorization") String authorization,
            @PathParam("actionId") String actionId) {

        Session session = null;
        try {

            session = extractSession(authorization);
            LOGGER.info("{}:{} [Schedule] Cancel scheduled action {}", request.getRemoteAddr(), session, actionId);
            scheduler.cancel(session, Integer.parseInt(actionId));
            return buildResponse("Success");

        } catch (ApiException e) {
            LOGGER.error("{}:{} [Schedule] Cancel scheduled action {} - {}", request.getRemoteAddr(), session, actionId, e.getMessage());
            return buildErrorResponse(e.getStatusCode(), e.getMessage());
        } catch (Exception e) {
            LOGGER.error(request.getRemoteAddr() + ":" + session + " [Schedule] Cancel scheduled action " + actionId + " - Unknown server error", e);
            return buildErrorResponse(500, e.getMessage());
        }
    }

}
