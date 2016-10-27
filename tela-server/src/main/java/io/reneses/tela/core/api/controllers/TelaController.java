package io.reneses.tela.core.api.controllers;

import io.reneses.tela.core.api.exceptions.AuthorizationApiException;
import io.reneses.tela.core.api.exceptions.ParameterException;
import io.reneses.tela.core.dispatcher.ActionDispatcher;
import io.reneses.tela.core.scheduler.Scheduler;
import io.reneses.tela.core.sessions.SessionManager;
import io.reneses.tela.core.sessions.models.Session;

import javax.ws.rs.core.Response;
import java.util.Map;

/**
 * The API Service contains logic used by the the API controllers
 */
public abstract class TelaController {

    protected static SessionManager sessionManager;
    protected static ActionDispatcher dispatcher;
    protected static Scheduler scheduler;

    /**
     * Configure (inject) the components that the controllers will use
     * This has to be done before any controller is instantiated (this is, before starting the server)
     *
     * @param telaSessionManager Session manager
     * @param telaDispatcher     Action dispatcher
     * @param telaScheduler      Scheduler
     */
    public static void configureComponents(SessionManager telaSessionManager,
                                           ActionDispatcher telaDispatcher,
                                           Scheduler telaScheduler) {

        sessionManager = telaSessionManager;
        dispatcher = telaDispatcher;
        scheduler = telaScheduler;
    }

    /**
     * Constructor for TelaController.
     */
    public TelaController() {
        if (sessionManager == null)
            throw new IllegalStateException("The Session Manager has not been configured");
        if (dispatcher == null)
            throw new IllegalStateException("The Action Dispatcher has not been configured");
        if (scheduler == null)
            throw new IllegalStateException("The Scheduler has not been configured");
    }

    /**
     * Extract the session from the authorization header
     *
     * @param authorizationHeader Authorization header
     * @return Session Authorization session
     * @throws AuthorizationApiException If the header is incorrect or the session is not found
     */
    protected Session extractSession(String authorizationHeader) throws AuthorizationApiException {
        if (authorizationHeader == null)
            throw new AuthorizationApiException("Authorization header required");
        if (!authorizationHeader.startsWith("Bearer"))
            throw new AuthorizationApiException("Authorization method not supported");
        String accessToken = authorizationHeader.substring("Bearer ".length());
        return sessionManager.findByAccessToken(accessToken);
    }

    /**
     * Extract a parameter or return a default value
     *
     * @param params       Params
     * @param params       Params
     * @param param        Name of the parameter to extract
     * @param defaultValue Default value
     * @return Param value if present, default value otherwise
     */
    protected static String getParameter(Map<String, String[]> params, String param, String defaultValue) {
        String[] valueArray = params.getOrDefault(param, null);
        if (valueArray == null || valueArray.length == 0 || valueArray[0].isEmpty())
            return defaultValue;
        return valueArray[0];
    }

    /**
     * Extract an integer parameter, or return a default value
     *
     * @param params       Params
     * @param params       Params
     * @param param        Name of the parameter to extract
     * @param defaultValue Default value
     * @return Param value if present, default value otherwise
     * @throws ParameterException If the parameter is not a valid integer
     */
    protected static Integer getIntParameter(Map<String, String[]> params, String param, Integer defaultValue) throws ParameterException {
        try {
            String value = getParameter(params, param, null);
            if (value == null)
                return defaultValue;
            return Integer.valueOf(value);
        } catch (NumberFormatException e) {
            throw new ParameterException("The parameter '" + param + "' must be an integer");
        }
    }

    /**
     * Build a success response
     *
     * @param result Result
     * @return Response
     */
    protected static Response buildResponse(Object result) {
        return Response.ok(result)
                .build();
    }

    /**
     * Build an error response
     *
     * @param code    Error code
     * @param message Error message
     * @return Error response
     */
    protected static Response buildErrorResponse(int code, String message) {
        return Response
                .status(code)
                .entity(message)
                .build();
    }

}
