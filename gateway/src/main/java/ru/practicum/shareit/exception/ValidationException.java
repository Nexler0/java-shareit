package ru.practicum.shareit.exception;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ValidationException extends RuntimeException {

    private final String message;

    @Override
    public String getMessage() {
        return message;
    }
}
