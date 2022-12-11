package com.learning.nk.service;

import com.learning.nk.dto.NoteDTO;
import com.learning.nk.exception.UnsupportedAuthenticationException;
import com.learning.nk.model.CustomUser;
import com.learning.nk.repository.NoteRepo;
import com.learning.nk.repository.UserRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;


@Service
public class NoteService {
    final NoteRepo noteRepo;
    final UserRepo userRepo;
    Logger logger = LoggerFactory.getLogger(NoteService.class);

    @Autowired
    public NoteService(NoteRepo noteRepo , UserRepo userRepo) {
        this.noteRepo = noteRepo;
        this.userRepo = userRepo;
    }

    public void upload(NoteDTO noteDTO) {
        CustomUser userDetails = getUserDetails();
        noteRepo.save(noteDTO.toNote(userDetails));
    }

    public Collection<NoteDTO> getAll() {
        CustomUser userDetails = getUserDetails();
        return userDetails.getNotes()
                .stream()
                .map(NoteDTO::fromNote)
                .collect(Collectors.toList());
    }

    public CustomUser getUserDetails() {
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();
        Class<?> clazz = authentication.getPrincipal()
                .getClass();
        Class<CustomUser> customUserClass = CustomUser.class;
        if (clazz.equals(customUserClass)) {
            String name = authentication.getName();
            return userRepo.getByUsername(name);
        }
        throw new UnsupportedAuthenticationException();
    }
}
