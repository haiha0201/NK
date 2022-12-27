package com.learning.nk.controller;

import com.learning.nk.dto.NoteDTO;
import com.learning.nk.service.NoteServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/api/notes")
public class NoteController {
    final NoteServiceInterface noteService;

    @Autowired
    public NoteController(NoteServiceInterface noteService) {this.noteService = noteService;}

    @PostMapping
    public void upload(@RequestBody NoteDTO noteDTO) {
        noteService.upload(noteDTO);
    }

    @PutMapping
    public void put(@RequestBody NoteDTO noteDTO) {
        noteService.put(noteDTO);
    }

    @DeleteMapping
    public void delete(Long id) {
        noteService.delete(id);
    }

    @GetMapping("/suggest")
    public ResponseEntity<Collection<NoteDTO>> suggest(@RequestParam String query) {
        Collection<NoteDTO> suggest = noteService.suggest(query);
        return ResponseEntity.ok(suggest);
    }

    @GetMapping
    public ResponseEntity<Collection<NoteDTO>> get() {
        Collection<NoteDTO> noteDTOS = noteService.getAll();
        return ResponseEntity.ok(noteDTOS);
    }

}
