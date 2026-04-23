package com.smartcampus.exception;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

@Provider
public class GlobalExceptionMapper 
    implements ExceptionMapper<Throwable> {

    private static final Logger LOG = 
        Logger.getLogger(GlobalExceptionMapper.class.getName());

    @Override
    public Response toResponse(Throwable e) {
        LOG.severe("Unexpected error: " + e.getMessage());
        Map<String, String> error = new HashMap<>();
        error.put("error", "INTERNAL_SERVER_ERROR");
        error.put("message", 
            "An unexpected error occurred. Please contact support.");
        return Response.status(500)
            .entity(error)
            .type(MediaType.APPLICATION_JSON)
            .build();
    }
}