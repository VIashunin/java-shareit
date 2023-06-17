package ru.practicum.shareit.exceptions;

public class PageableParametersAreInvalidException extends RuntimeException {
    public PageableParametersAreInvalidException(String message) {
        super(message);
    }
}
