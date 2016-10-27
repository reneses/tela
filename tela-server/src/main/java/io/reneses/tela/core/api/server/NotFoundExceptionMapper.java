package io.reneses.tela.core.api.server;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Custom 404 not found response
 */
@Provider
class NotFoundExceptionMapper implements ExceptionMapper<NotFoundException> {

    /**
     * toResponse.
     *
     * @param exception a {@link javax.ws.rs.NotFoundException} object.
     * @return a {@link javax.ws.rs.core.Response} object.
     */
    public Response toResponse(NotFoundException exception) {
        return Response
                .status(404)
                .entity("Not Found")
                .build();
    }

}
