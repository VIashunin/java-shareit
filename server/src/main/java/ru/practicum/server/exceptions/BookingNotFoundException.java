package ru.practicum.server.exceptions;

public class BookingNotFoundException extends RuntimeException {
    public BookingNotFoundException(long bookingId) {
        super("Booking with id: " + bookingId + " does not exist.");
    }
}
