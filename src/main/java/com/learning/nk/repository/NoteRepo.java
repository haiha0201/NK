package com.learning.nk.repository;

import com.learning.nk.model.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface NoteRepo extends JpaRepository<Note, Long> {
    Collection<Note> findNotesByContentStartsWith(String query);
}
