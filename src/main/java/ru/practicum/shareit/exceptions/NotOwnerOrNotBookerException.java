package ru.practicum.shareit.exceptions;

public class NotOwnerOrNotBookerException extends RuntimeException {
    public NotOwnerOrNotBookerException(String message) {
        super(message);
    }
}
