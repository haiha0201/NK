package com.learning.nk.controller;

import com.learning.nk.dto.NoteDTO;
import com.learning.nk.model.Note;
import com.learning.nk.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/api/notes")
public class NoteController {
    final NoteService noteService;

    @Autowired
    public NoteController(NoteService noteService) {this.noteService = noteService;}

    @PostMapping("")
    public void upload(@RequestBody NoteDTO noteDTO) {
        noteService.upload(noteDTO);
    }

    @GetMapping("")
    public ResponseEntity<Collection<NoteDTO>> get() {
        Collection<NoteDTO> noteDTOS = noteService.getAll();
        return ResponseEntity.ok(noteDTOS);
    }

}
