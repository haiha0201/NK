package com.learning.nk.controller;

import com.learning.nk.service.ImageServiceInterface;
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
    final
    ImageServiceInterface imageService;

    @Autowired
    public ImageController(ImageServiceInterface imageService) {this.imageService = imageService;}


    @PostMapping
    public ResponseEntity create(@RequestParam(name = "file") MultipartFile[] files) {

        for (MultipartFile file : files) {

            try {

                String fileName = imageService.save(file);

                String imageUrl = imageService.getImageUrl(fileName);

                // do whatever you want with that

            } catch (Exception e) {
                //  throw internal error;
            }
        }

        return ResponseEntity.ok().build();
    }
}
