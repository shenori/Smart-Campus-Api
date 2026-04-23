package com.smartcampus.exception;

public class LinkedResourceNotFoundException extends RuntimeException {
    public LinkedResourceNotFoundException(String roomId) {
        super("Room with ID '" + roomId + 
            "' does not exist. Cannot assign sensor.");
    }
}