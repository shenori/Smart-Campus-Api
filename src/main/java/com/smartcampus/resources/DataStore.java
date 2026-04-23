package com.smartcampus.resources;

import com.smartcampus.model.Room;
import com.smartcampus.model.Sensor;
import com.smartcampus.model.SensorReading;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * DataStore - Singleton In-Memory Data Store
 * 
 * This class acts as the central data store for the entire application.
 * It uses the Singleton pattern to ensure only ONE instance exists,
 * shared across all resource classes.
 */
public class DataStore {

    // The single shared instance - created once when class loads
    private static final DataStore INSTANCE = new DataStore();

    // In-memory storage for Rooms - key is room ID (e.g. "LIB-301")
    public final Map<String, Room> rooms = new ConcurrentHashMap<>();

    // In-memory storage for Sensors - key is sensor ID (e.g. "CO2-001")
    public final Map<String, Sensor> sensors = new ConcurrentHashMap<>();

    // In-memory storage for Readings - key is sensor ID
    // Each sensor has a list of historical readings
    public final Map<String, List<SensorReading>> readings = 
        new ConcurrentHashMap<>();

    /**
     * Private constructor - prevents anyone from creating
     * a second DataStore instance using "new DataStore()"
     */
    private DataStore() {}

    /**
     * Returns the single shared DataStore instance
     * All resource classes call this to access shared data
     * 
     * @return the singleton DataStore instance
     */
    public static DataStore getInstance() {
        return INSTANCE;
    }
}