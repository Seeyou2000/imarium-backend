package com.imarium.controller;

import com.imarium.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/artwork")
public class ArtworkController {

    @Autowired
    private FileService fileService;

    // 여러 이미지 업로드 API
    @PostMapping("/{artworkId}/upload")
    public ResponseEntity<List<String>> uploadArtworkImages(@PathVariable Long artworkId,
                                                            @RequestParam("files") List<MultipartFile> files) {
        try {
            List<String> imageUrls = fileService.saveFiles(files, artworkId);
            return ResponseEntity.status(HttpStatus.CREATED).body(imageUrls);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
