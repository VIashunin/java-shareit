package ru.practicum.server.exceptions;

public class ItemNotAvailableException extends RuntimeException {
    public ItemNotAvailableException(long itemId) {
        super("Item with id: " + itemId + " is not available.");
    }
}
