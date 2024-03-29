package ru.practicum.shareit.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;

@Slf4j
@RestControllerAdvice("ru.practicum.shareit")
public class ErrorHandler {

    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMethodArgumentNotValidException(final MethodArgumentNotValidException e) {
        String field = Objects.requireNonNull(e.getBindingResult().getFieldError()).getField();
        String errorMessage = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        String message = String.format("Поле %s %s", field, errorMessage);

        log.error(message);

        return new ErrorResponse(message);
    }

    @ExceptionHandler({NotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFound(final RuntimeException e) {
        log.error(e.getMessage());

        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler({BadRequestException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBadRequestException(final RuntimeException e) {
        log.error(e.getMessage());

        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleThrowable(final Throwable e) {
        log.error(e.getLocalizedMessage());

        return new ErrorResponse(e.getLocalizedMessage());
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    private static class ErrorResponse {
        String error;
    }
}
