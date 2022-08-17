package ru.practicum.shareit.exception;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler({ValidationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST) //400
    public ErrorResponse incorrectParameter(final ValidationException e) {
        return new ErrorResponse(e.getMessage(), "400");
    }

    @ExceptionHandler({NotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND) //404
    public ErrorResponse incorrectParameter(final NotFoundException e) {
        return new ErrorResponse(e.getMessage(), "404");
    }

    @ExceptionHandler({ConflictException.class})
    @ResponseStatus(HttpStatus.CONFLICT) //409
    public ErrorResponse incorrectParameter(final ConflictException e) {
        return new ErrorResponse(e.getMessage(), "409");
    }

    @ExceptionHandler({EmptyListException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) //500
    public ErrorResponse incorrectParameter(final RuntimeException e) {
        return new ErrorResponse(e.getMessage(), "500");
    }

}

@Data
class ErrorResponse {
    private final String error;
    private final String description;
}
