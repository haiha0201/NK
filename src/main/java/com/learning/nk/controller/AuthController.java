package com.learning.nk.controller;

import com.learning.nk.dto.AuthenticateDTO;
import com.learning.nk.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
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
    public ResponseEntity<String> login(HttpServletRequest request ,
                                        @RequestBody AuthenticateDTO authenticateDTO) {
        authenticate.login(request, authenticateDTO);
        return ResponseEntity.ok("success");
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signup(HttpServletRequest request ,
                                         @RequestBody AuthenticateDTO authenticateDTO) {
        authenticate.signup(request, authenticateDTO);
        return ResponseEntity.ok("success");
    }
}
