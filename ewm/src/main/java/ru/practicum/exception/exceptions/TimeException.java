package ru.practicum.exception.exceptions;

import javax.xml.bind.ValidationException;

public class TimeException extends RuntimeException {
    public TimeException(String message) {
        super(message);
    }
}
