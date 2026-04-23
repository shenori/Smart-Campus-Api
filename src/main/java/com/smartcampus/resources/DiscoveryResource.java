package com.smartcampus.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Discovery Resource - Part 1, Task 2
 * 
 * This implements the HATEOAS principle by returning links
 * to all available resources in the API.
 * 
 * Clients can use this endpoint to navigate the entire API
 * without needing to know URLs in advance.
 */
@Path("/")
public class DiscoveryResource {

    /**
     * 
     * Returns API metadata including:
     * - Version information
     * - Contact details
     * - Links to all primary resource collections (HATEOAS)
     * 
     * @return 200 OK with JSON metadata
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response discover() {

        // Build the links map - this is the HATEOAS part
        // Clients can use these links to navigate the API
        Map<String, String> links = new LinkedHashMap<>();
        links.put("rooms", "/api/v1/rooms");
        links.put("sensors", "/api/v1/sensors");

        // Build the full response body
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("version", "1.0");
        response.put("name", "Smart Campus API");
        response.put("description", 
            "RESTful API for managing university campus rooms and sensors");
        response.put("contact", "admin@smartcampus.ac.uk");
        response.put("links", links); // HATEOAS navigation links

        // Return 200 OK with the metadata
        return Response.ok(response).build();
    }
}