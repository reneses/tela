package io.reneses.tela.core.api.controllers;


import io.reneses.tela.core.api.exceptions.ApiException;
import io.reneses.tela.core.api.exceptions.RequiredParameterException;
import io.reneses.tela.core.dispatcher.exceptions.ModuleNotDefinedException;
import io.reneses.tela.core.sessions.models.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Auth controller
 */
@Path("/auth")
public class AuthController extends TelaController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);

    /**
     * Constructor for AuthController.
     */
    public AuthController() {
        super();
    }

    /**
     * Create a session
     *
     * @param request HTTP request
     * @param module  Module token
     * @param token   Token
     * @return Access token
     */
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(
            @Context HttpServletRequest request,
            @FormParam("module") String module,
            @FormParam("token") String token) {

        try {

            // Create the session without module token
            if (module == null && token == null) {
                LOGGER.info("{} [Auth] Create session", request.getRemoteAddr());
                String accessToken = sessionManager.create().getAccessToken();
                return buildResponse(accessToken);
            }

            // Create with module token
            if (module == null || module.isEmpty() || token == null || token.isEmpty())
                throw new RequiredParameterException("Authorization parameters invalid");
            if (!dispatcher.hasModule(module))
                throw new ModuleNotDefinedException(module);
            String accessToken = sessionManager.createWithModuleToken(module, token).getAccessToken();
            LOGGER.info("{} [Auth] Create session (access token '{}') with '{}' token '{}'", request.getRemoteAddr(), accessToken, module, token);
            return buildResponse(accessToken);

        } catch (ApiException e) {
            LOGGER.error(String.format("%s [Auth] Create session with '%s' token '%s' - %s", request.getRemoteAddr(), module, token, e.getMessage()), e);
            return buildErrorResponse(e.getStatusCode(), e.getMessage());
        } catch (Exception e) {
            LOGGER.error(String.format("%s [Auth] Create session with '%s' token '%s' - Unknown server error", request.getRemoteAddr(), module, token), e);
            return buildErrorResponse(500, e.getMessage());
        }
    }

    /**
     * Delete a session
     *
     * @param request       HTTP request
     * @param authorization Authorization header
     * @return Success response
     */
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(
            @Context HttpServletRequest request,
            @HeaderParam("Authorization") String authorization) {

        Session session = null;
        try {
            session = extractSession(authorization);
            LOGGER.info("{}:{} [Auth] Delete session", request.getRemoteAddr(), session);
            sessionManager.delete(session);
            return buildResponse("Success");
        } catch (ApiException e) {
            LOGGER.error(String.format("%s:%s [Auth] Delete session - %s", request.getRemoteAddr(), session, e.getMessage()), e);
            return buildErrorResponse(e.getStatusCode(), e.getMessage());
        } catch (Exception e) {
            LOGGER.error(String.format("%s:%s [Auth] Delete session - Unknown server error", request.getRemoteAddr(), session), e);
            return buildErrorResponse(500, e.getMessage());
        }

    }

    /**
     * Add a module token to the current session
     *
     * @param request       HTTP request
     * @param authorization Authorization header
     * @param module        Module
     * @param token         Token
     * @return Success response
     */
    @POST
    @Path("/{module}")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addToken(
            @Context HttpServletRequest request,
            @HeaderParam("Authorization") String authorization,
            @PathParam("module") String module,
            @QueryParam("token") String token) {

        Session session = null;
        try {

            session = extractSession(authorization);
            if (!dispatcher.hasModule(module))
                throw new ModuleNotDefinedException(module);
            if (token == null || token.isEmpty())
                throw new RequiredParameterException("Token required");
            LOGGER.info("{}:{} [Auth] Edit session add '{}' token '{}'", request.getRemoteAddr(), module, token);
            sessionManager.addToken(session, module, token);
            return buildResponse("Success");

        } catch (ApiException e) {
            LOGGER.error(String.format("%s:%s [Auth] Edit session add '%s' token '%s' - %s",
                    request.getRemoteAddr(), session, module, token, e.getMessage()), e);
            return buildErrorResponse(e.getStatusCode(), e.getMessage());
        } catch (Exception e) {
            LOGGER.error(String.format("%s:%s [Auth] Edit session add '%s' token '%s' - Unknown server error",
                    request.getRemoteAddr(), session, module, token), e);
            return buildErrorResponse(500, e.getMessage());
        }

    }

    /**
     * Delete a module token from the current user
     *
     * @param request       HTTP request
     * @param authorization Authorization header
     * @param module        Module
     * @return Success response
     */
    @DELETE
    @Path("/{module}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteToken(
            @Context HttpServletRequest request,
            @HeaderParam("Authorization") String authorization,
            @PathParam("module") String module) {

        Session session = null;
        try {
            session = extractSession(authorization);
            LOGGER.info("{}:{} [Auth] Delete '{}' token", request.getRemoteAddr(), session, module);
            sessionManager.deleteModuleToken(session, module);
            return buildResponse("Success");

        } catch (ApiException e) {
            LOGGER.error(String.format("%s:%s [Auth] Delete '%s' token - %s",
                    request.getRemoteAddr(), session, module, e.getMessage()), e);
            return buildErrorResponse(e.getStatusCode(), e.getMessage());
        } catch (Exception e) {
            LOGGER.error(String.format("%s:%s [Auth] Delete '%s' token - Unknown server error",
                    request.getRemoteAddr(), session, module), e);
            return buildErrorResponse(500, e.getMessage());
        }

    }

}
