package com.poriadin.portfolio.controllers;

import com.poriadin.portfolio.services.UserService;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@CrossOrigin
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/avatars/{avatarPath}")
    public ResponseEntity<byte[]> getUserAvatar(@PathVariable String avatarPath) {
        byte[] avatarBytes = userService.loadAvatarBytes(avatarPath);
        if (avatarBytes != null) {
            try {
                String extension = getExtension(avatarPath);
                String contentType = determineContentType(extension);

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.parseMediaType(contentType));
                headers.setContentDisposition(ContentDisposition.builder("inline").filename(avatarPath).build());

                return new ResponseEntity<>(avatarBytes, headers, HttpStatus.OK);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    private String getExtension(String filename) {
        Path path = Paths.get(filename);
        String extension = "";

        if (filename.contains(".")) {
            extension = filename.substring(filename.lastIndexOf(".") + 1);
        }

        return extension;
    }

    private String determineContentType(String extension) {
        return switch (extension.toLowerCase()) {
            case "jpg", "jpeg" -> "image/jpeg";
            case "png" -> "image/png";
            case "gif" -> "image/gif";
            // Add more cases for other supported image formats if needed
            default -> "application/octet-stream";
        };
    }

}
