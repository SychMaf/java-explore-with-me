package ru.practicum.exception.exceptions;

public class EventPatchConflictException extends RuntimeException{
    public EventPatchConflictException(String message) {
        super(message);
    }
}
