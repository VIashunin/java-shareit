package ru.practicum.server.exceptions;

public class BookingOfOwnItemException extends RuntimeException {
    public BookingOfOwnItemException() {
        super("You can't book your own item.");
    }
}
