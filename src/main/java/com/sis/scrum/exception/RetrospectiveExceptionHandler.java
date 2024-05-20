package com.sis.scrum.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.sis.scrum.model.ExceptionStatus;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.format.DateTimeParseException;
import java.util.Arrays;

@ControllerAdvice
@Slf4j
public class RetrospectiveExceptionHandler {
    @ExceptionHandler(value = {RetrospectiveNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected ResponseEntity<Object> handleNotFound(Exception ex) {
        return new ResponseEntity<>(new ExceptionStatus(HttpStatus.NOT_FOUND, ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {FeedbackAlreadyExistException.class, RetrospectiveAlreadyExistException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    protected ResponseEntity<Object> handleAlreadyExist(Exception ex) {
        return new ResponseEntity<>(new ExceptionStatus(HttpStatus.CONFLICT, ex.getMessage()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = {DateTimeParseException.class, ValidationException.class})
    protected ResponseEntity<Object> handleBadRequest(Exception ex) {
        return new ResponseEntity<>(new ExceptionStatus(HttpStatus.BAD_REQUEST, ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {NotAuthorisedUserException.class})
    protected ResponseEntity<Object> handleUnauthorizedUser(Exception ex) {
        return new ResponseEntity<>(new ExceptionStatus(HttpStatus.UNAUTHORIZED, ex.getMessage()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(value = {HttpMessageNotReadableException.class})
    public ResponseEntity<ExceptionStatus> handleValidationException(HttpMessageNotReadableException exception) {
        String errorDetails = "";

        if (exception.getCause() instanceof InvalidFormatException) {
            InvalidFormatException ifx = (InvalidFormatException) exception.getCause();
            if (ifx.getTargetType() != null && ifx.getTargetType().isEnum()) {
                errorDetails = String.format("Invalid enum value: '%s' for the field: '%s'. The value must be one of: %s.",
                        ifx.getValue(), ifx.getPath().get(ifx.getPath().size() - 1).getFieldName(), Arrays.toString(ifx.getTargetType().getEnumConstants()));
            }
        }
        ExceptionStatus errorResponse = new ExceptionStatus(HttpStatus.BAD_REQUEST, errorDetails);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {Exception.class})
    protected ResponseEntity<Object> handleExceptionRequest(Exception ex) {
        return new ResponseEntity<>(new ExceptionStatus(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
