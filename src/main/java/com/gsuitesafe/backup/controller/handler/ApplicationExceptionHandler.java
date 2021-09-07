package com.gsuitesafe.backup.controller.handler;

import com.gsuitesafe.backup.exception.BackupNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ApplicationExceptionHandler {

    @ExceptionHandler(BackupNotFoundException.class)
    public ResponseEntity<StandardError> resourceNotFoundErrorHandler(
            BackupNotFoundException ex,
            HttpServletRequest request
    ) {
        StandardError error = new StandardError(System.currentTimeMillis(), HttpStatus.NOT_FOUND.value(),
                "Resource was not found.", ex.getMessage(), request.getRequestURI());

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
}
