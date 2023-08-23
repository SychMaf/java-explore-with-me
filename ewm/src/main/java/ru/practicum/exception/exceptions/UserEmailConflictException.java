package ru.practicum.exception.exceptions;

public class UserEmailConflictException extends RuntimeException {

    public UserEmailConflictException(String message) {
        super(message);
    }
}
