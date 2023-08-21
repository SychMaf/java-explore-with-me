package ru.practicum.exception.exceptions;

public class RequestCreatedConflictException extends RuntimeException {
    public RequestCreatedConflictException(String message) {
        super(message);
    }
}
