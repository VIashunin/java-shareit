package ru.practicum.shareit.exceptions;

public class EndBeforeOrEqualStartException extends RuntimeException {
    public EndBeforeOrEqualStartException() {
        super("End date and time can't be before start date and time.");
    }
}
