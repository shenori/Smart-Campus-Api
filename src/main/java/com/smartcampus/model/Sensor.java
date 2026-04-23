package com.smartcampus.model;


// PART 3: SENSOR MODEL (POJO)
// Represents a physical sensor device deployed in a campus room.
// Used across Part 3 (Sensor Operations) and Part 4 (Readings) 

public class Sensor {

    // Unique identifier for this sensor, e.g. "TEMP-001"
    private String id;

    // Category of sensor, e.g. "Temperature", "CO2", "Occupancy"
    private String type;

    // Current operational state: "ACTIVE", "MAINTENANCE", or "OFFLINE"
    // Used in Part 5.3 — sensors in MAINTENANCE cannot receive new readings
    private String status;

    // The most recent measurement recorded by this sensor
    // Updated automatically every time a new reading is posted (Part 4)
    private double currentValue;

    // Foreign key — links this sensor to the Room it is physically inside
    // Must match an existing Room ID in the DataStore (validated in Part 3.1)
    private String roomId;

    // Default no-arg constructor required by JAX-RS
    // JAX-RS uses this to deserialize incoming JSON into a Sensor object
    public Sensor() {}

    // --- Getters and Setters ---
    // Required for Jackson to serialize (Java → JSON) and
    // deserialize (JSON → Java) this object automatically

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public double getCurrentValue() { return currentValue; }
    public void setCurrentValue(double currentValue) {
        this.currentValue = currentValue;
    }

    public String getRoomId() { return roomId; }
    public void setRoomId(String roomId) { this.roomId = roomId; }
}