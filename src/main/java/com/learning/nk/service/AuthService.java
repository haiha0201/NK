package com.learning.nk.service;

import com.learning.nk.exception.RegisteredUserException;
import com.learning.nk.exception.UnregisteredUserException;
import com.learning.nk.model.AuthenticateRequest;
import com.learning.nk.model.CustomUser;
import com.learning.nk.repository.UserRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {
    final UserRepo userRepo;
    final AuthenticationManager authenticateManager;
    final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthService(UserRepo userRepo , AuthenticationManager authenticateManager ,
                       PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.authenticateManager = authenticateManager;
        this.passwordEncoder = passwordEncoder;
    }

    public void login(AuthenticateRequest authenticateRequest) {
        Optional<CustomUser> byUsername =
                userRepo.findByUsername(authenticateRequest.getUsername());
        if (byUsername.isEmpty()) throw new UnregisteredUserException();
        Authentication authentication = getAuthenticationToken(authenticateRequest);
        Authentication authenticate = authenticateManager.authenticate(authentication);
        putUserInSpringContext(authenticate);
    }

    @Transactional
    public void signup(AuthenticateRequest authenticateRequest) {
        Optional<CustomUser> byUsername =
                userRepo.findByUsername(authenticateRequest.getUsername());
        if (byUsername.isPresent()) throw new RegisteredUserException();
        saveUser(authenticateRequest);
        Authentication authentication = getAuthenticationToken(authenticateRequest);
        putUserInSpringContext(authentication);
    }

    private void saveUser(AuthenticateRequest authenticateRequest) {
        final String encryptedPass = passwordEncoder.encode(authenticateRequest.getPassword());
        userRepo.save(CustomUser.builder()
                .username(authenticateRequest.getUsername())
                .password(encryptedPass)
                .build());
    }

    private void putUserInSpringContext(Authentication authenticate) {
        SecurityContextHolder
                .getContext()
                .setAuthentication(authenticate);
    }

    private Authentication getAuthenticationToken(AuthenticateRequest authenticateRequest) {
        Authentication token = new UsernamePasswordAuthenticationToken(
                authenticateRequest.getUsername() ,
                authenticateRequest.getPassword()
        );
        return token;
    }
}
