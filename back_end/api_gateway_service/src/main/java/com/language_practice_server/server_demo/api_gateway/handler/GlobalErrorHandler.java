package com.language_practice_server.server_demo.api_gateway.handler;

import com.language_practice_server.server_demo.api_gateway.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.time.Instant;

//TODO: Reactive ErrorWebExceptionHandler (advanced, for Gateway filters & routes)

@ControllerAdvice
public class GlobalErrorHandler {
    //TODO: add logger here
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAllExceptions(Exception ex) {
        ErrorResponse error = new ErrorResponse(
                Instant.now().toString(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                ex.getMessage(),
                ""
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
