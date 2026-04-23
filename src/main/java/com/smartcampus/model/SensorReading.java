package com.smartcampus.model;

/**
 * SensorReading Model - Core Data Entity
 */
public class SensorReading {

    // Unique identifier for this reading event
    // Generated automatically as a UUID when reading is created
    private String id;

    // Epoch time in milliseconds when the reading was captured
    // Set automatically to System.currentTimeMillis() on creation
    private long timestamp;

    // The actual measured value recorded by the sensor hardware
    // e.g. 450.5 for CO2 ppm, 23.5 for temperature in Celsius
    private double value;

    public SensorReading() {}

    // Getters and Setters

    public String getId() {
        return id; 
    }
    
    public void setId(String id) {
        this.id = id; 
    }

    public long getTimestamp() {
        return timestamp; 
    }
    
    public void setTimestamp(long timestamp) { 
        this.timestamp = timestamp; 
    }

    public double getValue() { return value; }
    public void setValue(double value) { this.value = value; }
}

