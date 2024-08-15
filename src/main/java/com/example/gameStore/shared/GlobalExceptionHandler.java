package com.example.gameStore.shared;

import com.example.gameStore.dtos.GlobalResponse;
import com.example.gameStore.shared.exceptions.BadRequestException;
import com.example.gameStore.shared.exceptions.NoContentException;
import com.example.gameStore.shared.exceptions.ResourceAlreadyExistsException;
import com.example.gameStore.shared.exceptions.ResourceNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.springframework.http.HttpStatus.*;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<GlobalResponse<Void>> handleBadRequestException(RuntimeException e) {
        return ResponseEntity.badRequest().body(new GlobalResponse<>(BAD_REQUEST.value(), e.getMessage()));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<GlobalResponse<Void>> handleResourceNotFoundException(RuntimeException e) {
        return ResponseEntity
                .status(NOT_FOUND)
                .body(new GlobalResponse<>(NOT_FOUND.value(), e.getMessage()));
    }

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<GlobalResponse<Void>> handleResourceAlreadyExistsException(RuntimeException e) {
        return ResponseEntity.badRequest().body(new GlobalResponse<>(BAD_REQUEST.value(), e.getMessage()));
    }

    @ExceptionHandler(NoContentException.class)
    public ResponseEntity<GlobalResponse<Void>> handleNoContentException(RuntimeException e) {
        return ResponseEntity.status(NO_CONTENT).body(new GlobalResponse<>(NO_CONTENT.value(), e.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<GlobalResponse<Void>> handleIllegalArgumentException(RuntimeException e) {
        return ResponseEntity.badRequest().body(new GlobalResponse<>(BAD_REQUEST.value(), e.getMessage()));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<GlobalResponse<Void>> handleRuntimeException(RuntimeException e) {
        return ResponseEntity.internalServerError().body(new GlobalResponse<>(INTERNAL_SERVER_ERROR.value(), e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<GlobalResponse<Void>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String errorMessage = Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage();
        return ResponseEntity.badRequest().body(new GlobalResponse<>(BAD_REQUEST.value(), errorMessage));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<GlobalResponse<Void>> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        String errorMessage = e.getMessage();
        String userFriendlyMessage = parseErrorMessage(errorMessage);
        return ResponseEntity.badRequest().body(new GlobalResponse<>(BAD_REQUEST.value(), userFriendlyMessage));
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<GlobalResponse<Void>> handleAuthenticationException(AuthenticationException e) {
        return ResponseEntity.status(FORBIDDEN).body(new GlobalResponse<>(FORBIDDEN.value(), e.getMessage()));
    }

    private String parseErrorMessage(String errorMessage) {
        Pattern pattern = Pattern.compile("Key \\((.+?)\\)=\\((.+?)\\) already exists");
        Matcher matcher = pattern.matcher(errorMessage);

        if (matcher.find()) {
            String field = matcher.group(1);
            String value = matcher.group(2);
            return String.format("A user with the %s '%s' already exists.", field, value);
        }

        return "An error occurred while processing your request.";
    }
}
