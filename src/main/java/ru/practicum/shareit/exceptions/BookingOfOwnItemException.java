package ru.practicum.shareit.exceptions;

public class BookingOfOwnItemException extends RuntimeException {
    public BookingOfOwnItemException() {
        super("You can't book your own item.");
    }
}
