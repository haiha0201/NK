package com.learning.nk.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @GetMapping("/{id}")
    public ResponseEntity getUserInfo(@PathVariable String id) {
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();
        System.out.println(authentication);
        return ResponseEntity
                .noContent()
                .build();
    }
}
