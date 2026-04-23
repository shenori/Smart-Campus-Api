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

public class SensorReadingResource {

    private final String sensorId;
    private DataStore store = DataStore.getInstance();

    public SensorReadingResource(String sensorId) {
        this.sensorId = sensorId;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getReadings() {
        List<SensorReading> list = 
            store.readings.getOrDefault(sensorId, new ArrayList<>());
        return Response.ok(list).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addReading(SensorReading reading) {
        Sensor sensor = store.sensors.get(sensorId);

        if (sensor == null) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Sensor not found");
            return Response.status(404).entity(error).build();
        }

        if ("MAINTENANCE".equals(sensor.getStatus())) {
            throw new SensorUnavailableException(sensorId);
        }

        reading.setId(UUID.randomUUID().toString());
        reading.setTimestamp(System.currentTimeMillis());
        store.readings.get(sensorId).add(reading);

        // Update sensor current value
        sensor.setCurrentValue(reading.getValue());

        return Response.status(201).entity(reading).build();
    }
}