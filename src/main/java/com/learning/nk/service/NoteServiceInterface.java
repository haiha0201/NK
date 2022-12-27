package com.learning.nk.service;

import com.learning.nk.dto.NoteDTO;
import com.learning.nk.model.CustomUser;

import java.util.Collection;

public interface NoteServiceInterface {
    void put(NoteDTO noteDTO);

    void upload(NoteDTO noteDTO);

    void delete(Long id);

    Collection<NoteDTO> suggest(String query);

    Collection<NoteDTO> getAll();
}
