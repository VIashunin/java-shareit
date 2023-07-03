package ru.practicum.server.exceptions;

public class PageableParametersAreInvalidException extends RuntimeException {
    public PageableParametersAreInvalidException(String message) {
        super(message);
    }
}
