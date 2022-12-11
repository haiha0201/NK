package com.learning.nk.service;

import com.learning.nk.exception.UnregisteredUserException;
import com.learning.nk.model.CustomUser;
import com.learning.nk.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    final UserRepo userRepo;
    @Autowired
    public CustomUserDetailsService(UserRepo userRepo) {this.userRepo = userRepo;}

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<CustomUser> byUsername = userRepo.findByUsername(username);
        if (byUsername.isEmpty()) throw new UnregisteredUserException();
        return byUsername.get();
    }
}
