package ru.practicum.server.exceptions;

public class NotOwnerOrNotBookerException extends RuntimeException {
    public NotOwnerOrNotBookerException(String message) {
        super(message);
    }
}
