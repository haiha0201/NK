package com.learning.nk.controller;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
@ControllerAdvice
public class GlobalResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
    Logger logger = LoggerFactory.getLogger(GlobalResponseEntityExceptionHandler.class);

    @ExceptionHandler({Exception.class})
    public ResponseEntity<FailResponseEntity> handleIOException(Exception e) {
        logger.error(e.getMessage());
        FailResponseEntity failedResponseEntity = new FailResponseEntity(e.getMessage());
        return ResponseEntity.status(404)
                .body(failedResponseEntity);
    }

    @Data
    public static class FailResponseEntity {
        private String message;

        public FailResponseEntity(String message) {this.message = message;}
    }
}
