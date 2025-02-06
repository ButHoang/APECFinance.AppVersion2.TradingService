package com.apec_finance.trading.exception.base_exception;

import com.apec_finance.trading.comon.CommonErrorCodes;
import com.apec_finance.trading.comon.ErrorResponse;
import com.apec_finance.trading.comon.ResponseBuilder;
import com.apec_finance.trading.exception.validate.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseBuilder<String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new ResponseBuilder<>(HttpStatus.BAD_REQUEST.value(), "Validation error", errors.toString());
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseBuilder<ErrorResponse> handleValidationException(ValidationException ex) {
        log.error("Validation error: {}", ex.getMessage());
        var errRs = new ErrorResponse(CommonErrorCodes.VALIDATION_ERROR.getCode(), ex.getMessage());
        return new ResponseBuilder<>(HttpStatus.BAD_REQUEST.value(), "Validation error", errRs);
    }
}
