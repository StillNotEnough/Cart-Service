package com.amazingshop.personal.cartservice.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {

    private String message;
    private long timestamp;
    private String path;
    private int status;
    private Map<String, String> errors;

    public ErrorResponse(String message, long timestamp) {
        this.message = message;
        this.timestamp = timestamp;
    }

    public ErrorResponse(String message, long timestamp, String path, int status) {
        this.message = message;
        this.timestamp = timestamp;
        this.path = path;
        this.status = status;
    }

    public ErrorResponse(String message, Map<String, String> errors) {
        this.message = message;
        this.errors = errors;
        this.timestamp = System.currentTimeMillis();
    }

    public ErrorResponse(String message) {
        this.message = message;
        this.timestamp = System.currentTimeMillis();
    }

    public static ErrorResponse makeErrorResponse(String message) {
        return new ErrorResponse(message, System.currentTimeMillis());
    }

    public static ErrorResponse makeErrorResponse(String message, String path, int status) {
        return new ErrorResponse(message, System.currentTimeMillis(), path, status);
    }

    public static ErrorResponse makeErrorResponse(String message, Map<String, String> errors) {
        return new ErrorResponse(message, errors);
    }

    public static ErrorResponse makeValidationErrorResponse(Map<String, String> validationErrors) {
        return new ErrorResponse("Validation failed", validationErrors);
    }
}