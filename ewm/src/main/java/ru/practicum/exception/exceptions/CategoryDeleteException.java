package ru.practicum.exception.exceptions;

public class CategoryDeleteException extends RuntimeException {
    public CategoryDeleteException(String message) {
        super(message);
    }
}
