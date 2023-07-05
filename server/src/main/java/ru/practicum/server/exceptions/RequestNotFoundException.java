package ru.practicum.server.exceptions;

public class RequestNotFoundException extends RuntimeException {
    public RequestNotFoundException(long requestId) {
        super("Request with id: " + requestId + " was not found.");
    }
}
