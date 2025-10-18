package com.gshelgaas.messageserver.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ErrorHandler {

    private static final DateTimeFormatter TIMESTAMP_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @ExceptionHandler(ProtobufProcessingException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleProtobufProcessingException(final ProtobufProcessingException e) {
        return buildApiError(
                e.getMessage(),
                "Ошибка обработки Protobuf сообщения",
                HttpStatus.BAD_REQUEST,
                Collections.singletonList(e.getMessage())
        );
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleMissingHeader(final MissingRequestHeaderException e) {
        return buildApiError(
                "Missing header: " + e.getHeaderName(),
                "Incorrectly made request.",
                HttpStatus.BAD_REQUEST,
                Collections.singletonList("Отсутствует обязательный заголовок: " + e.getHeaderName())
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleMethodArgumentNotValid(final MethodArgumentNotValidException e) {
        List<String> errors = e.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.toList());

        return buildApiError(
                "Validation failed",
                "Incorrectly made request.",
                HttpStatus.BAD_REQUEST,
                errors
        );
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handleErrors(final Exception e) {
        return buildApiError(
                "Internal server error",
                "Произошла непредвиденная ошибка.",
                HttpStatus.INTERNAL_SERVER_ERROR,
                Collections.singletonList(e.getMessage())
        );
    }

    private ApiError buildApiError(String message, String reason, HttpStatus status, List<String> errors) {
        return ApiError.builder()
                .message(message)
                .reason(reason)
                .status(status.name())
                .timestamp(LocalDateTime.now().format(TIMESTAMP_FORMATTER))
                .errors(errors)
                .build();
    }
}