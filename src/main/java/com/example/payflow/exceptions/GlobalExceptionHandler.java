package com.example.payflow.exceptions;

import com.example.payflow.dto.ErrorResponse;
import com.example.payflow.exceptions.InsufficientFundsException;
import com.example.payflow.exceptions.WalletNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(InsufficientFundsException.class)
    public ResponseEntity<ErrorResponse> handle(InsufficientFundsException ex) {
        return build(HttpStatus.CONFLICT, "INSUFFICIENT_FUNDS", ex.getMessage());
    }

    @ExceptionHandler(WalletNotFoundException.class)
    public ResponseEntity<ErrorResponse> handle(WalletNotFoundException ex) {
        return build(HttpStatus.NOT_FOUND, "WALLET_NOT_FOUND", ex.getMessage());
    }

    @ExceptionHandler(UsernameAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handle(UsernameAlreadyExistsException ex) {
        return build(HttpStatus.BAD_REQUEST, "USERNAME_ALREADY_EXISTS", ex.getMessage());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handle(BadCredentialsException ex) {
        return build(HttpStatus.UNAUTHORIZED, "INVALID_CREDENTIALS", ex.getMessage());
    }

    @ExceptionHandler(ForbiddenAction.class)
    public ResponseEntity<ErrorResponse> handle(ForbiddenAction ex) {
        return build(HttpStatus.FORBIDDEN, "FORBIDDEN", ex.getMessage());
    }



    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handle(EmailAlreadyExistsException ex) {
        return build(HttpStatus.BAD_REQUEST, "EMAIL_ALREADY_EXISTS", ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnknown(Exception ex) {
        log.error("Unexpected error", ex);
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_ERROR", "Something went wrong");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(e -> errors.put(e.getField(), e.getDefaultMessage()));
        return buildWithDetails(HttpStatus.BAD_REQUEST, "VALIDATION_FAILED", "Invalid request", errors);
    }





    private ResponseEntity<ErrorResponse> build(HttpStatus status, String code, String msg) {
        return ResponseEntity.status(status)
                .body(new ErrorResponse(Instant.now(), status.value(), code, msg, null));
    }

    private ResponseEntity<ErrorResponse> buildWithDetails(HttpStatus status, String code,
                                                           String msg, Map<String, String> details) {
        return ResponseEntity.status(status)
                .body(new ErrorResponse(Instant.now(), status.value(), code, msg, details));
    }
}