package com.smartcampus;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

/**
 * JAX-RS Application Configuration Class
 * 
 * This class bootstraps the JAX-RS application.
 * The @ApplicationPath annotation sets the base URL for all API endpoints.
 */
@ApplicationPath("/api/v1")
public class JAXRSConfiguration extends Application {
    // No additional configuration needed
    // JAX-RS automatically scans and registers all resource classes
}