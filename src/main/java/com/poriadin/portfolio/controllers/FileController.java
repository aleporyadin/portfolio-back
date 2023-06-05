package com.poriadin.portfolio.controllers;


import com.poriadin.portfolio.entities.File;
import com.poriadin.portfolio.services.FileService;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;

@RestController
@CrossOrigin
@RequestMapping("/api/files")
public class FileController {
    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Page<File>> getUserFiles(@PathVariable Integer userId,
                                                   @RequestParam(defaultValue = "0") int page,
                                                   @RequestParam(defaultValue = "10") int size) {
        Page<File> files = fileService.getUserFiles(userId, page, size);
        return ResponseEntity.ok(files);
    }

    @PostMapping("/{userId}")
    public ResponseEntity<String> uploadFile(@PathVariable Integer userId, @RequestParam("file") MultipartFile file) {
        try {
            fileService.uploadFile(userId, file);
            return ResponseEntity.ok("File uploaded successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(216).body(e.getMessage());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/{userId}/{fileId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Integer userId, @PathVariable Integer fileId) {
        try {
            Resource fileResource = fileService.downloadFile(userId, fileId);

            // Get the file extension
            String extension = getFileExtension(fileResource.getFilename());

            // Set the appropriate content type based on the extension
            String contentType = fileService.getContentType(extension);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileResource.getFilename() + "\"")
                    .contentType(MediaType.parseMediaType(contentType))
                    .body(fileResource);
        } catch (FileNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    private String getFileExtension(String filename) {
        return filename.substring(filename.lastIndexOf(".") + 1);
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

