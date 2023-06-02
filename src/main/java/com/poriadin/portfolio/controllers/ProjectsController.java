package com.poriadin.portfolio.controllers;


import com.poriadin.portfolio.entities.User;
import com.poriadin.portfolio.entities.ZipFile;
import com.poriadin.portfolio.repositories.UserRepository;
import com.poriadin.portfolio.repositories.ZipFileRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@RequestMapping("/api/files")
public class ProjectsController {

    @Value("${upload.directory}")
    private String uploadDirectory;
    private final Path UPLOAD_DIR =  Paths.get("/src/main/resources/zips/");;

    private final UserRepository userRepository;

    private final ZipFileRepository zipFileRepository;

    public ProjectsController(UserRepository userRepository, ZipFileRepository zipFileRepository) {
        this.userRepository = userRepository;
        this.zipFileRepository = zipFileRepository;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile file, @RequestParam("userId") Integer userId) {
        try {
            String fileName = file.getOriginalFilename();
            //UPLOAD_DIR.getRoot();
            System.out.println(UPLOAD_DIR);
            Optional<User> optionalUser = userRepository.findById(userId);
            if (optionalUser.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not found.");
            }

            User user = optionalUser.get();

            // Create a new ZipFile entity
            ZipFile zipFile = new ZipFile();
            zipFile.setFileName(fileName);
            zipFile.setUser(user);

            // Save the zip file
            zipFileRepository.save(zipFile);

            File targetFile = new File(UPLOAD_DIR + fileName);
            file.transferTo(targetFile);

            return ResponseEntity.ok("File uploaded successfully!");
        } catch (IOException e) {
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload file.");
        }
    }

    @GetMapping("/list/{userId}")
    public ResponseEntity<List<String>> getFileList(@PathVariable Integer userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.badRequest().body(Collections.emptyList());
        }

        User user = optionalUser.get();

        List<ZipFile> zipFiles = user.getZipFiles();

        List<String> fileList = zipFiles.stream()
                .map(ZipFile::getFileName)
                .collect(Collectors.toList());

        return ResponseEntity.ok(fileList);
    }

    @GetMapping("/download/{userId}/{fileName}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Integer userId, @PathVariable String fileName) throws FileNotFoundException {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        User user = optionalUser.get();

        // Check if the user has the specified zip file
        Optional<ZipFile> optionalZipFile = user.getZipFiles().stream()
                .filter(zipFile -> zipFile.getFileName().equals(fileName))
                .findFirst();

        if (optionalZipFile.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        File file = new File(UPLOAD_DIR + fileName);
        if (!file.exists()) {
            return ResponseEntity.notFound().build();
        }

        InputStreamResource resource;
        try {
            resource = new InputStreamResource(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentDispositionFormData("attachment", file.getName());
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{userId}/{fileName}")
    public ResponseEntity<String> deleteFile(@PathVariable Integer userId, @PathVariable String fileName) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not found.");
        }

        User user = optionalUser.get();

        // Check if the user has the specified zip file
        Optional<ZipFile> optionalZipFile = user.getZipFiles().stream()
                .filter(zipFile -> zipFile.getFileName().equals(fileName))
                .findFirst();

        if (optionalZipFile.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        File file = new File(UPLOAD_DIR + fileName);
        if (file.delete()) {
            // Delete the zip file entity
            zipFileRepository.delete(optionalZipFile.get());
            return ResponseEntity.ok("File deleted successfully!");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete file.");
        }
    }

    // Add a method to handle file renaming
    // ...
}

