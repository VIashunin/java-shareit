package ru.practicum.server.exceptions;

public class UnknownBookingStateException extends RuntimeException {
    public UnknownBookingStateException(String message) {
        super(message);
    }
}
