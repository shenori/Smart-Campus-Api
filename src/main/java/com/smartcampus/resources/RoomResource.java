package com.smartcampus.resources;

// =====================================================================
// PART 2: ROOM MANAGEMENT
// This file handles all room-related API endpoints
// Base path: /api/v1/rooms

import com.smartcampus.exception.RoomNotEmptyException;
import com.smartcampus.model.Room;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

// @Path("/rooms") — this class handles everything under /api/v1/rooms
// @Produces — all responses will be in JSON format
// @Consumes — this class expects incoming data to be in JSON format
@Path("/rooms")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RoomResource {

    // DataStore is our in-memory database (like a HashMap instead of SQL)
    // getInstance() gives us the same shared data across all requests
    private DataStore store = DataStore.getInstance();


    // PART 2.1 — GET ALL ROOMS
    // Endpoint: GET /api/v1/rooms
    // Returns: a list of all rooms currently stored in memory
    @GET
    public Response getAllRooms() {
        // store.rooms is a ConcurrentHashMap — .values() gives us all Room objects
        return Response.ok(store.rooms.values()).build();  // Status:  200 OK
    }


    // PART 2.1 — CREATE A NEW ROOM
    // Endpoint: POST /api/v1/rooms
    // Expects:  JSON body with id, name, capacity
    // Returns:  the created room with 201 Created
    //           or an error if ID is missing / already exists
    @POST
    public Response createRoom(Room room) {

        // VALIDATION: Room must have an ID — reject if missing or empty
        if (room.getId() == null || room.getId().isEmpty()) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Room ID is required");
            return Response.status(400).entity(error).build(); // 400 Bad Request
        }

        // DUPLICATE CHECK: If a room with this ID already exists, reject it
        if (store.rooms.containsKey(room.getId())) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Room already exists");
            return Response.status(409).entity(error).build(); // 409 Conflict
        }

        // SUCCESS: Save the room into our in-memory store using its ID as the key
        store.rooms.put(room.getId(), room);

        // Return the created room with 201 Created status
        return Response.status(201).entity(room).build();
    }


    // PART 2.1 — GET ONE SPECIFIC ROOM
    // Endpoint: GET /api/v1/rooms/{roomId}
    // Example:  GET /api/v1/rooms/LIB-301
    @GET
    @Path("/{roomId}")
    public Response getRoom(@PathParam("roomId") String roomId) {

        // Look up the room by its ID in the in-memory store
        Room room = store.rooms.get(roomId);

        // If no room found, return 404 with an error message
        if (room == null) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Room not found");
            return Response.status(404).entity(error).build(); // 404 Not Found
        }

        // Room found — return it with 200 OK
        return Response.ok(room).build();
    }


    // PART 2.2 — DELETE A ROOM (with safety logic)
    // Endpoint: DELETE /api/v1/rooms/{roomId}
    // Example:  DELETE /api/v1/rooms/LIB-301
    @DELETE
    @Path("/{roomId}")
    public Response deleteRoom(@PathParam("roomId") String roomId) {

        // Look up the room by its ID
        Room room = store.rooms.get(roomId);

        // SAFETY CHECK 1: Room must exist before we try to delete it
        if (room == null) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Room not found");
            return Response.status(404).entity(error).build(); // 404 Not Found
        }

        // SAFETY CHECK 2 (Business Logic — Part 2.2):
        // A room CANNOT be deleted if it still has sensors inside it
        // This prevents "orphan" sensors with no room to belong to
        // RoomNotEmptyException is caught by RoomNotEmptyMapper → returns 409 Conflict
        if (!room.getSensorIds().isEmpty()) {
            throw new RoomNotEmptyException(roomId);
        }

        // All checks passed — remove the room from memory
        store.rooms.remove(roomId);

        // Return success message with 200 OK
        Map<String, String> response = new HashMap<>();
        response.put("message", "Room '" + roomId + "' deleted successfully.");
        return Response.ok(response).build();
    }
}