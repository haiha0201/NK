package com.learning.nk.service;

import com.learning.nk.exception.RegisteredUserException;
import com.learning.nk.exception.UnregisteredUserException;
import com.learning.nk.dto.AuthenticateDTO;
import com.learning.nk.model.CustomUser;
import com.learning.nk.repository.UserRepo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;

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

    public void login(HttpServletRequest req, AuthenticateDTO authenticateDTO) {
        Optional<CustomUser> byUsername =
                userRepo.findByUsername(authenticateDTO.getUsername());
        if (byUsername.isEmpty()) throw new UnregisteredUserException();
        Authentication authentication = getAuthenticationToken(authenticateDTO);
        Authentication authenticate = authenticateManager.authenticate(authentication);
        putUserInSpringContext(req, authenticate);
    }

    @Transactional
    public void signup(HttpServletRequest req,AuthenticateDTO authenticateDTO) {
        Optional<CustomUser> byUsername =
                userRepo.findByUsername(authenticateDTO.getUsername());
        if (byUsername.isPresent()) throw new RegisteredUserException();
        saveUser(authenticateDTO);
        Authentication authentication = getAuthenticationToken(authenticateDTO);
        putUserInSpringContext(req, authentication);
    }

    private void saveUser(AuthenticateDTO authenticateDTO) {
        final String encryptedPass = passwordEncoder.encode(authenticateDTO.getPassword());
        userRepo.save(CustomUser.builder()
                .username(authenticateDTO.getUsername())
                .password(encryptedPass)
                .build());
    }

    private void putUserInSpringContext(HttpServletRequest req, Authentication authenticate) {
        SecurityContext sc = SecurityContextHolder
                .getContext();
        sc.setAuthentication(authenticate);
        HttpSession session = req.getSession(true);
        session.setAttribute(SPRING_SECURITY_CONTEXT_KEY, sc);
    }

    private Authentication getAuthenticationToken(AuthenticateDTO authenticateDTO) {
        Authentication token = new UsernamePasswordAuthenticationToken(
                authenticateDTO.getUsername() ,
                authenticateDTO.getPassword()
        );
        return token;
    }
}
