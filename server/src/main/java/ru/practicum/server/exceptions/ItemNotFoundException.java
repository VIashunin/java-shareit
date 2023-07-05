package ru.practicum.server.exceptions;

public class ItemNotFoundException extends RuntimeException {
    public ItemNotFoundException(Long itemId) {
        super("Item with id: " + itemId + " does not exist.");
    }
}
