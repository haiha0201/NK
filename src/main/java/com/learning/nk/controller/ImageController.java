package com.learning.nk.controller;

import com.learning.nk.service.ImageServiceInterface;
import com.learning.nk.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/images")
public class ImageController {
    final ImageServiceInterface imageService;
    final NoteService noteService;

    @Autowired
    public ImageController(ImageServiceInterface imageService , NoteService noteService) {
        this.imageService = imageService;
        this.noteService = noteService;
    }


    @PostMapping
    public ResponseEntity<String> create(@RequestParam(name = "file") MultipartFile file) {
        try {
            String fileName = imageService.save(file);
            String imageUrl = imageService.getImageUrl(fileName);
            String content = noteService.parseContent(imageUrl);
            return ResponseEntity.ok(content);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
