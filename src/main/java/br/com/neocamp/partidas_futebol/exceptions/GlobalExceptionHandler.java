package br.com.neocamp.partidas_futebol.exceptions;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiError> handleEntityNotFoundException(EntityNotFoundException ex) {
        ApiError error = new ApiError(ex.getMessage(), "NOT_FOUND");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(EntityConflictException.class)
    public ResponseEntity<ApiError> handleEntityConflictException(EntityConflictException ex) {
        ApiError error = new ApiError(ex.getMessage(), "CONFLICT");
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(EntityBadRequestException.class)
    public ResponseEntity<ApiError> handleEntityBadRequestException(EntityBadRequestException ex) {
        ApiError error = new ApiError(ex.getMessage(), "BAD_REQUEST");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

}
