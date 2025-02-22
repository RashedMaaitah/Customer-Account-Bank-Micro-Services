package io.rashed.bank.exception;

import io.rashed.bank.common.api.response.ApiResponse;
import io.rashed.bank.common.exception.EntityAlreadyExistsException;
import io.rashed.bank.common.exception.ExceptionHandlerBase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Slf4j
@RestControllerAdvice
public class ApplicationExceptionHandler extends ExceptionHandlerBase {

    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<ApiResponse<String>> handleCustomerNotFoundException(
            CustomerNotFoundException ex) {
        log.error("Customer Not Found exception\n", ex);

        return ResponseEntity.status(NOT_FOUND)
                .body(
                        ApiResponse.error("Customer not found",
                                List.of(ex.getMessage()),
                                NOT_FOUND.value())
                );
    }
}
