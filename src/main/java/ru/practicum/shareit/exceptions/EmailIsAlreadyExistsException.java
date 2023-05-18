package ru.practicum.shareit.exceptions;

public class EmailIsAlreadyExistsException extends RuntimeException {
    public EmailIsAlreadyExistsException(String email) {
        super("Email: <<" + email + ">> is already exists.");
    }
}
