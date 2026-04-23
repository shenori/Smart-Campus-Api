package com.smartcampus.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Room Model - Core Data Entity
 * 
 * Represents a physical room in the Smart Campus system.
 * Each room can contain multiple sensors for monitoring
 * temperature, CO2, occupancy etc.
 */
public class Room {

    // Unique identifier for the room e.g. "LIB-301"
    private String id;

    // Human readable name e.g. "Library Quiet Study"
    private String name;

    // Maximum number of people allowed for safety regulations
    private int capacity;

    // List of sensor IDs deployed in this room
    // Maintained automatically when sensors are added/removed
    private List<String> sensorIds = new ArrayList<>();
    public Room() {}

    // Getters and Setters below
    // Required for Jackson to serialize/deserialize JSON

    public String getId() {
        return id; 
    }
    
    public void setId(String id) {
        this.id = id; 
    }

    public String getName() {
        return name; 
    }
    
    public void setName(String name) {
        this.name = name; 
    }

    public int getCapacity() {
        return capacity; 
    }
    
    public void setCapacity(int capacity) {
        this.capacity = capacity; 
    }

    public List<String> getSensorIds() {
        return sensorIds; 
    }
    
    public void setSensorIds(List<String> sensorIds) { 
        this.sensorIds = sensorIds; 
    }
}