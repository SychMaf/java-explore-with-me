package ru.practicum.exception.exceptions;

public class EventStateConflictException extends RuntimeException {
    public EventStateConflictException(String message) {
        super(message);
    }
}
