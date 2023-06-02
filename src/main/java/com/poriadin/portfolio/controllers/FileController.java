package com.poriadin.portfolio.controllers;


import com.poriadin.portfolio.entities.File;
import com.poriadin.portfolio.services.FileService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/files")
public class FileController {
    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<File>> getUserFiles(@PathVariable Integer userId) {
        List<File> files = fileService.getUserFiles(userId);
        return ResponseEntity.ok(files);
    }

    @PostMapping("/{userId}")
    public ResponseEntity<String> uploadFile(@PathVariable Integer userId, @RequestParam("file") MultipartFile file) {
        try {
            fileService.uploadFile(userId, file);
            return ResponseEntity.ok("File uploaded successfully.");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload file.");
        }
    }

    @GetMapping("/{userId}/{fileId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Integer userId, @PathVariable Integer fileId) {
        try {
            Resource fileResource = fileService.downloadFile(userId, fileId);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileResource.getFilename() + "\"")
                    .body(fileResource);
        } catch (FileNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{userId}/{fileId}")
    public ResponseEntity<String> deleteFile(@PathVariable Integer userId, @PathVariable Integer fileId) {
        boolean success = fileService.deleteFile(userId, fileId);
        if (success) {
            return ResponseEntity.ok("File deleted successfully.");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{userId}/{fileId}")
    public ResponseEntity<String> renameFile(@PathVariable Integer userId, @PathVariable Integer fileId, @RequestParam String newName) {
        boolean success = fileService.renameFile(userId, fileId, newName);
        if (success) {
            return ResponseEntity.ok("File renamed successfully.");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

