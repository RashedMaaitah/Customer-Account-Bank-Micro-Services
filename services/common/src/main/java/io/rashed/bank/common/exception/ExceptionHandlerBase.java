package io.rashed.bank.common.exception;

import io.rashed.bank.common.api.response.ApiResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.*;

@Slf4j
//@RestControllerAdvice
@RequiredArgsConstructor
public class ExceptionHandlerBase {

    @ExceptionHandler(EntityAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<String>> handleEntityAlreadyExistsException(
            EntityAlreadyExistsException ex) {
        log.error("Entity already exists exception\n", ex);

        return ResponseEntity.status(CONFLICT)
                .body(
                        ApiResponse.error("Entity already exists",
                                List.of(ex.getMessage()),
                                CONFLICT.value())
                );
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiResponse<String>> handleResourceNotFoundException(
            RuntimeException ex) {
        log.error("Resource not found \n", ex);

        return ResponseEntity.status(NOT_FOUND)
                .body(
                        ApiResponse.error("Entity not found",
                                List.of(ex.getMessage()),
                                NOT_FOUND.value()));
    }


    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<String>> handleAccessDeniedException(
            AccessDeniedException ex) {
        log.error("Access denied error: \n", ex);

        return ResponseEntity
                .status(FORBIDDEN)
                .body(
                        ApiResponse.error("Access denied.",
                                List.of(ex.getMessage()),
                                FORBIDDEN.value()));
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<String>> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException ex) {
        log.error("Method Argument Not Valid Exception \n", ex);

        List<String> errors = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.toList());

        return ResponseEntity
                .status(BAD_REQUEST)
                .body(
                        ApiResponse.error("Validation errors occurred",
                                errors,
                                BAD_REQUEST.value()));
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<String>> handleGeneralException(
            Exception ex) {
        log.error("Unexpected error \n", ex);

        return ResponseEntity
                .status(INTERNAL_SERVER_ERROR)
                .body(
                        ApiResponse.error("An unexpected error occurred",
                                List.of(ex.getMessage()),
                                INTERNAL_SERVER_ERROR.value()));
    }
}
