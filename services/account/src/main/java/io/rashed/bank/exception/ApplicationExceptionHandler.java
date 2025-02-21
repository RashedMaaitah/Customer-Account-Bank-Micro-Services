package io.rashed.bank.exception;

import io.rashed.bank.common.exception.ExceptionHandlerBase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ApplicationExceptionHandler extends ExceptionHandlerBase {
}
