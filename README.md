# Smart Campus API

A RESTful API for managing university campus rooms and sensors, built with JAX-RS (Jersey) and deployed on Apache Tomcat.

---

## API Overview

| Base URL | `http://localhost:8080/SmartCampus/api/v1` |
|---|---|
| Protocol | HTTP/1.1 |
| Format | JSON |
| Framework | JAX-RS (Jersey) on Apache Tomcat |

### Resource Hierarchy

```
/api/v1
├── /                          → Discovery (HATEOAS)
├── /rooms                     → Room management
│   └── /{roomId}              → Single room operations
└── /sensors                   → Sensor management
    └── /{sensorId}/readings   → Sensor reading history (sub-resource)
```

---

## How to Build & Run

### Prerequisites

- Java 8+
- Maven 3.6+
- Apache Tomcat 9.x

### Steps

```bash
# 1. Clone the repository
git clone <your-github-repo-url>
cd SmartCampus

# 2. Build the WAR file
mvn clean package

# 3. Deploy to Tomcat
# Copy the generated WAR to your Tomcat webapps directory:
cp target/SmartCampus-1.0-SNAPSHOT.war $TOMCAT_HOME/webapps/SmartCampus.war

# 4. Start Tomcat
$TOMCAT_HOME/bin/startup.sh     # Linux/macOS
$TOMCAT_HOME/bin/startup.bat    # Windows

# 5. API is now live at:
# http://localhost:8080/SmartCampus/api/v1
```

---

## Full API Endpoint Reference

### Part 1 — Discovery

| Method | URL | Description |
|--------|-----|-------------|
| GET | `/api/v1/` | API metadata + HATEOAS links |

### Part 2 — Room Management

| Method | URL | Description |
|--------|-----|-------------|
| GET | `/api/v1/rooms` | Get all rooms |
| POST | `/api/v1/rooms` | Create a new room |
| GET | `/api/v1/rooms/{roomId}` | Get a specific room |
| DELETE | `/api/v1/rooms/{roomId}` | Delete a room (blocked if sensors exist → 409) |

### Part 3 — Sensor Operations

| Method | URL | Description |
|--------|-----|-------------|
| GET | `/api/v1/sensors` | Get all sensors |
| GET | `/api/v1/sensors?type={type}` | Filter sensors by type (e.g. `CO2`, `Temperature`) |
| POST | `/api/v1/sensors` | Register a new sensor (roomId must exist → else 422) |

### Part 4 — Sensor Readings (Sub-Resource)

| Method | URL | Description |
|--------|-----|-------------|
| GET | `/api/v1/sensors/{sensorId}/readings` | Get all readings for a sensor |
| POST | `/api/v1/sensors/{sensorId}/readings` | Add a new reading (blocked if MAINTENANCE → 403) |

---

## Sample curl Commands

### 1. Discovery Endpoint
```bash
curl -X GET http://localhost:8080/SmartCampus/api/v1/
```

**Expected Response (200 OK):**
```json
{
  "version": "1.0",
  "name": "Smart Campus API",
  "description": "RESTful API for managing university campus rooms and sensors",
  "contact": "admin@smartcampus.ac.uk",
  "links": {
    "rooms": "/api/v1/rooms",
    "sensors": "/api/v1/sensors"
  }
}
```

---

### 2. Create a Room
```bash
curl -X POST http://localhost:8080/SmartCampus/api/v1/rooms \
  -H "Content-Type: application/json" \
  -d '{
    "id": "LIB-301",
    "name": "Library Quiet Study",
    "capacity": 50
  }'
```

**Expected Response (201 Created):**
```json
{
  "id": "LIB-301",
  "name": "Library Quiet Study",
  "capacity": 50,
  "sensorIds": []
}
```

---

### 3. Get All Rooms
```bash
curl -X GET http://localhost:8080/SmartCampus/api/v1/rooms
```

**Expected Response (200 OK):**
```json
[
    {
        "id": "LAB-A",
        "name": "Lab A",
        "capacity": 30,
        "sensorIds": []
    },
    {
        "id": "LIB-301",
        "name": "Library Quiet Study",
        "capacity": 50,
        "sensorIds": []
    }
]
```

---

### 4. Get a Specific Room
```bash
curl -X GET http://localhost:8080/SmartCampus/api/v1/rooms/LIB-301
```

**Expected Response (200 OK):**
```json
{
  "id": "LIB-301",
  "name": "Library Quiet Study",
  "capacity": 50,
  "sensorIds": []
}
```

---

### 5. Create a Sensor (linked to a room)
```bash
curl -X POST http://localhost:8080/SmartCampus/api/v1/sensors \
  -H "Content-Type: application/json" \
  -d '{
    "id": "CO2-001",
    "type": "CO2",
    "status": "ACTIVE",
    "currentValue": 0.0,
    "roomId": "LIB-301"
  }'
```

**Expected Response (201 Created):**
```json
{
  "id": "CO2-001",
  "type": "CO2",
  "status": "ACTIVE",
  "currentValue": 0.0,
  "roomId": "LIB-301"
}
```

---

### 6. Get All Sensors (with optional type filter)
```bash
# All sensors
curl -X GET http://localhost:8080/SmartCampus/api/v1/sensors

# Filter by type
curl -X GET "http://localhost:8080/SmartCampus/api/v1/sensors?type=CO2"
```

---

### 7. Post a Sensor Reading
```bash
curl -X POST http://localhost:8080/SmartCampus/api/v1/sensors/CO2-001/readings \
  -H "Content-Type: application/json" \
  -d '{
    "value": 450.5
  }'
```

**Expected Response (201 Created):**
```json
{
    "id": "2bcf6774-9421-4937-ace2-ed47053b8cba",
    "timestamp": 1776967471686,
    "value": 450.5
}

```

---

### 8. Get All Readings for a Sensor
```bash
curl -X GET http://localhost:8080/SmartCampus/api/v1/sensors/CO2-001/readings
```
'
[
    {
        "id": "2bcf6774-9421-4937-ace2-ed47053b8cba",
        "timestamp": 1776967471686,
        "value": 450.5
    }
]
'


---

### 9. Try to Delete a Room with Sensors (triggers 409 Conflict)
```bash
curl -X DELETE http://localhost:8080/SmartCampus/api/v1/rooms/LIB-301
```

**Expected Response (409 Conflict):**
```json
{
    "error": "CONFLICT",
    "message": "Room LIB-301 cannot be deleted as it still has sensors assigned."
}
```

---

### 10. Try to Add a Sensor with a Non-Existent Room (triggers 422)
```bash
curl -X POST http://localhost:8080/SmartCampus/api/v1/sensors \
  -H "Content-Type: application/json" \
  -d '{
    "id": "TEMP-999",
    "type": "Temperature",
    "status": "ACTIVE",
    "currentValue": 0.0,
    "roomId": "FAKE-ROOM"
  }'
```

**Expected Response :**
[
    {
        "id": "CO2-001",
        "type": "CO2",
        "status": "ACTIVE",
        "currentValue": 450.5,
        "roomId": "LIB-301"
    }
]


**Expected Response (422 Unprocessable Entity):**
```json
{
  "error": "Linked resource 'FAKE-ROOM' was not found."
}
```

---

### 11. Post a Reading to a Sensor in MAINTENANCE (triggers 403)
```bash
# First set a sensor to MAINTENANCE status, then attempt a reading:
curl -X POST http://localhost:8080/SmartCampus/api/v1/sensors/CO2-001/readings \
  -H "Content-Type: application/json" \
  -d '{"value": 500.0}'
```
**Expected Response (201 created):**
{
    "id": "11d22331-7c41-432c-990b-cf4d1181d367",
    "timestamp": 1776968749740,
    "value": 500.0
}


**Expected Response (403 Forbidden):**
```json
{
  "error": "Sensor 'CO2-001' is currently under maintenance and cannot accept readings."
}
```

---

## Error Reference

| HTTP Status | Scenario |
|-------------|----------|
| 400 Bad Request | Missing required fields (e.g. room ID not provided) |
| 403 Forbidden | POST reading to a sensor in MAINTENANCE status |
| 404 Not Found | Room or sensor with given ID does not exist |
| 409 Conflict | Attempting to delete a room that still has sensors |
| 422 Unprocessable Entity | Creating a sensor with a roomId that doesn't exist |
| 500 Internal Server Error | Unexpected runtime error (global catch-all mapper) |

---

