package com.learning.nk.controller;

import com.learning.nk.dto.AuthenticateDTO;
import com.learning.nk.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.ObjectFactory;
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

    final
    ObjectFactory<HttpSession> httpSessionFactory;

    @Autowired
    public AuthController(AuthService authenticate ,
                          ObjectFactory<HttpSession> httpSessionFactory) {
        this.authenticate = authenticate;
        this.httpSessionFactory = httpSessionFactory;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(HttpServletRequest request ,
                                        @RequestBody AuthenticateDTO authenticateDTO) {
        authenticate.login(request , authenticateDTO);
        return ResponseEntity.ok("success");
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        httpSessionFactory.getObject()
                .invalidate();
        return ResponseEntity.ok("success");
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signup(HttpServletRequest request ,
                                         @RequestBody AuthenticateDTO authenticateDTO) {
        authenticate.signup(request , authenticateDTO);
        return ResponseEntity.ok("success");
    }
}
