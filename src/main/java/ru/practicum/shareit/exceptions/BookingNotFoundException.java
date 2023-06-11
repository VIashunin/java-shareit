package ru.practicum.shareit.exceptions;

public class BookingNotFoundException extends RuntimeException {
    public BookingNotFoundException(long bookingId) {
        super("Booking with id: " + bookingId + " does not exist.");
    }
}
