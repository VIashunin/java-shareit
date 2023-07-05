package ru.practicum.server.exceptions;

public class ItemWasNotBookedEarlierException extends RuntimeException {
    public ItemWasNotBookedEarlierException() {
        super("This item was not booked by you earlier.");
    }
}
