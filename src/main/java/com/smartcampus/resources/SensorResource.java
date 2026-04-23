package com.smartcampus.resources;

import com.smartcampus.exception.LinkedResourceNotFoundException;
import com.smartcampus.model.Sensor;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Path("/sensors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorResource {

    private DataStore store = DataStore.getInstance();

    @GET
    public Response getSensors(@QueryParam("type") String type) {
        List<Sensor> result = new ArrayList<>(store.sensors.values());
        if (type != null) {
            result.removeIf(s -> 
                !s.getType().equalsIgnoreCase(type));
        }
        return Response.ok(result).build();
    }

    @POST
    public Response createSensor(Sensor sensor) {
        if (!store.rooms.containsKey(sensor.getRoomId())) {
            throw new LinkedResourceNotFoundException(sensor.getRoomId());
        }
        store.sensors.put(sensor.getId(), sensor);
        store.rooms.get(sensor.getRoomId())
            .getSensorIds().add(sensor.getId());
        store.readings.put(sensor.getId(), new ArrayList<>());
        return Response.status(201).entity(sensor).build();
    }

    @Path("/{sensorId}/readings")
    public SensorReadingResource getReadings(
            @PathParam("sensorId") String sensorId) {
        return new SensorReadingResource(sensorId);
    }
}