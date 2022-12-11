package com.learning.nk.controller;

import com.learning.nk.model.AuthenticateRequest;
import com.learning.nk.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    final AuthService authenticate;

    @Autowired
    public AuthController(AuthService authenticate) {this.authenticate = authenticate;}

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody AuthenticateRequest authenticateRequest) {
        authenticate.login(authenticateRequest);
        return ResponseEntity.ok("success");
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody AuthenticateRequest authenticateRequest) {
        authenticate.signup(authenticateRequest);
        return ResponseEntity.ok("success");
    }
}
