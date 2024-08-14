package com.example.gameStore.shared;

import com.example.gameStore.dtos.GlobalResponse;
import com.example.gameStore.shared.exceptions.BadRequestException;
import com.example.gameStore.shared.exceptions.NoContentException;
import com.example.gameStore.shared.exceptions.ResourceAlreadyExistsException;
import com.example.gameStore.shared.exceptions.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<GlobalResponse<Void>> handleBadRequestException(RuntimeException e) {
        return ResponseEntity.badRequest().body(new GlobalResponse<>(null, e.getMessage()));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<GlobalResponse<Void>> handleResourceNotFoundException(RuntimeException e) {
        return ResponseEntity
                .status(NOT_FOUND)
                .body(new GlobalResponse<>(null, e.getMessage()));
    }

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<GlobalResponse<Void>> handleResourceAlreadyExistsException(RuntimeException e) {
        return ResponseEntity.badRequest().body(new GlobalResponse<>(null, e.getMessage()));
    }

    @ExceptionHandler(NoContentException.class)
    public ResponseEntity<GlobalResponse<Void>> handleNoContentException(RuntimeException e) {
        return ResponseEntity.status(NO_CONTENT).body(new GlobalResponse<>(null, e.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<GlobalResponse<Void>> handleIllegalArgumentException(RuntimeException e) {
        return ResponseEntity.badRequest().body(new GlobalResponse<>(null, e.getMessage()));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<GlobalResponse<Void>> handleRuntimeException(RuntimeException e) {
        return ResponseEntity.internalServerError().body(new GlobalResponse<>(null, e.getMessage()));
    }
}
