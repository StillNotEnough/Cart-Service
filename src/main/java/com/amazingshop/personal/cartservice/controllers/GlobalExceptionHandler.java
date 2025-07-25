package com.amazingshop.personal.cartservice.controllers;

import com.amazingshop.personal.cartservice.util.exceptions.CartNotFoundException;
import com.amazingshop.personal.cartservice.util.exceptions.InsufficientStockException;
import com.amazingshop.personal.cartservice.util.exceptions.ProductNotFoundException;
import com.amazingshop.personal.cartservice.util.exceptions.UserNotFoundException;
import com.amazingshop.personal.cartservice.util.responses.ErrorResponse;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(CartNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlerCartNotFoundException(CartNotFoundException e){
        log.warn("Cart not found: {}", e.getMessage());
        return new ResponseEntity<>(ErrorResponse.makeErrorResponse("Cart not found: " + e.getMessage()),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlerProductNotFoundException(ProductNotFoundException e){
        log.warn("Product not found: {}", e.getMessage());
        return new ResponseEntity<>(ErrorResponse.makeErrorResponse("Product not found: " + e.getMessage()),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlerProductNotFoundException(UserNotFoundException e){
        log.warn("User not found: {}", e.getMessage());
        return new ResponseEntity<>(ErrorResponse.makeErrorResponse("User not found: " + e.getMessage()),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InsufficientStockException.class)
    public ResponseEntity<ErrorResponse> handlerInsufficientStockException(InsufficientStockException e){
        log.warn("Insufficient stock: {}", e.getMessage());
        return new ResponseEntity<>(ErrorResponse.makeErrorResponse("Insufficient stock: " + e.getMessage()),
                HttpStatus.BAD_REQUEST);
    }

    // Валидация @Valid в @RequestBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handlerMethodArgumentNotValidException(MethodArgumentNotValidException e){
        log.warn("Validation failed: {}", e.getMessage());

        Map<String, String> errors = new HashMap<>();

        e.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));

        return new ResponseEntity<>(ErrorResponse.makeErrorResponse("Validation failed", errors),
                HttpStatus.BAD_REQUEST);
    }

    // Валидация @PathVariable, @RequestParam
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(ConstraintViolationException e) {
        log.warn("Constraint violation: {}", e.getMessage());
        return new ResponseEntity<>(ErrorResponse.makeErrorResponse("Invalid parameter: " + e.getMessage()),
                HttpStatus.BAD_REQUEST);
    }

    // Неправильный тип параметра (например String вместо Long)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        log.warn("Type mismatch: {}", e.getMessage());
        String message = String.format("Invalid parameter '%s': expected %s but got '%s'",
                e.getName(), e.getRequiredType().getSimpleName(), e.getValue());
        return new ResponseEntity<>(ErrorResponse.makeErrorResponse(message),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException e) {
        log.warn("Illegal argument: {}", e.getMessage());
        return new ResponseEntity<>(ErrorResponse.makeErrorResponse("Invalid argument: " + e.getMessage()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handlerException(Exception e) {
        log.error("Unexpected error occurred", e);
        return new ResponseEntity<>(ErrorResponse.makeErrorResponse("An unexpected error occurred!"),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }
}