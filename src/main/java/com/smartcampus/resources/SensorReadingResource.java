package com.smartcampus.resources;

import com.smartcampus.exception.SensorUnavailableException;
import com.smartcampus.model.Sensor;
import com.smartcampus.model.SensorReading;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Resource class to manage sensor readings.
 * This is a sub-resource class (used inside SensorResource).
 */
public class SensorReadingResource {

    // ID of the parent sensor
    private final String sensorId;

    // Singleton DataStore instance (acts as in-memory database)
    private DataStore store = DataStore.getInstance();

    /**
     * Constructor receives sensorId from parent resource.
     */
    public SensorReadingResource(String sensorId) {
        this.sensorId = sensorId;
    }

    /**
     * GET method to retrieve all readings of a specific sensor.
     * URL: /sensors/{sensorId}/readings
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getReadings() {

        // Get readings list for this sensor
        // If no readings exist, return an empty list
        List<SensorReading> list =
            store.readings.getOrDefault(sensorId, new ArrayList<>());

        // Return 200 OK with readings list
        return Response.ok(list).build();
    }

    /**
     * POST method to add a new reading to a sensor.
     * URL: /sensors/{sensorId}/readings
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addReading(SensorReading reading) {

        // Check if sensor exists
        Sensor sensor = store.sensors.get(sensorId);

        if (sensor == null) {
            // If sensor not found, return 404 error
            Map<String, String> error = new HashMap<>();
            error.put("error", "Sensor not found");
            return Response.status(404).entity(error).build();
        }

        // If sensor status is MAINTENANCE, throw custom exception
        if ("MAINTENANCE".equals(sensor.getStatus())) {
            throw new SensorUnavailableException(sensorId);
        }

        // Generate unique ID for reading
        reading.setId(UUID.randomUUID().toString());

        // Set current system time as timestamp
        reading.setTimestamp(System.currentTimeMillis());

        // Add reading to the sensor's reading list
        store.readings.get(sensorId).add(reading);

        // Update sensor's current value with latest reading value
        sensor.setCurrentValue(reading.getValue());

        // Return 201 Created with created reading
        return Response.status(201).entity(reading).build();
    }
}