package ru.practicum.shareit.exceptions;

public class BookingAlreadyApprovedException extends RuntimeException {
    public BookingAlreadyApprovedException(long bookingId) {
        super("Booking with id: " + bookingId + " is already approved.");
    }
}
