package com.learning.nk.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/error")
public class ErrorController {
    @GetMapping("/unauthorized")
    public ResponseEntity<GlobalResponseEntityExceptionHandler.FailResponseEntity> get() {
        return ResponseEntity.status(401)
                .body(new GlobalResponseEntityExceptionHandler.FailResponseEntity(
                        "Unauthorized Request"));
    }
}