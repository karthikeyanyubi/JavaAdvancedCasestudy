package com.employee_service.exception;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.sql.SQLException;
import java.util.*;

import org.springframework.validation.FieldError;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<Map<String, String>> errorList = new ArrayList<>();

        if (ex.getBindingResult().getErrorCount() == 1) {
            Map<String, String> error  = new HashMap<>();
            String errorMessage = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();
            error.put("error", errorMessage);
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
        ex.getBindingResult().getAllErrors().forEach(errors -> {
            Map<String, String> error  = new HashMap<>();
            String fieldName = ((FieldError) errors).getField();
            String errorMessage = errors.getDefaultMessage();
            error.put("error", errorMessage);
            error.put("field_name",fieldName);
            errorList.add(error);
        });
        HashMap<String, Object> result = new HashMap<>();
        LOGGER.error("Global exception : " + errorList + " Time :"+new Date());
        result.put("errorList", errorList);
        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({HandlerMethodValidationException.class})
    public ResponseEntity<Object> handlerMethodValidationException(HandlerMethodValidationException ex) {
        StringBuilder errorMessage = new StringBuilder();
        ex.getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            errorMessage.append(fieldName).append(": ").append(message).append(". ");
        });
        return new ResponseEntity<>(errorMessage.toString(), HttpStatus.BAD_REQUEST);

    }


    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> handleAllExceptions(Exception exception) {
        LOGGER.error("Global exception : " + exception.getMessage() + " Time :"+new Date());
        HashMap<String, Object> result = new HashMap<>();
        result.put("error ", exception.getMessage());
        return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> handleConstraintViolationException(DataIntegrityViolationException ex) {
        Throwable rootCause = ex.getRootCause();
        LOGGER.error("Global exception : " + ex.getMessage() + " Time :"+new Date());
        Map<String, String> error = new HashMap<>();
        if (rootCause instanceof SQLException) {
            String sqlMessage = rootCause.getMessage();
            String constraintName = extractConstraintNameFromErrorMessage(sqlMessage);
            error.put("error", constraintName);

        }
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RuntimeException.class)
    public final ResponseEntity<Object> handleRuntimeExceptions(Exception exception) {
        LOGGER.error("Exception : " + exception.getMessage() + " Time :"+new Date());
        HashMap<String, Object> result = new HashMap<>();
        result.put("error", exception.getMessage());
        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpClientErrorException.Unauthorized.class)
    public final ResponseEntity<Object> handleUnauthorizedExceptions(Exception exception) {
        LOGGER.error("Exception : " + exception.getMessage() + " Time :"+new Date());
        HashMap<String, Object> result = new HashMap<>();
        result.put("error", exception.getMessage());
        return new ResponseEntity<>(result, HttpStatus.UNAUTHORIZED);
    }

    private String extractConstraintNameFromErrorMessage(String errorMessage) {
        int start = errorMessage.indexOf("(");
        int end = errorMessage.indexOf(")");
        String errorKey = errorMessage.substring(start, end+1);
        return errorKey;
    }
}


