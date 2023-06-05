package com.poriadin.portfolio.services;

import com.poriadin.portfolio.entities.File;
import com.poriadin.portfolio.entities.User;
import com.poriadin.portfolio.repositories.FileRepository;
import com.poriadin.portfolio.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

@Service
public class FileService {
    private final UserRepository userRepository;
    private final FileRepository fileRepository;
    private final Path fileStorageLocation;

    @Autowired
    public FileService(UserRepository userRepository, FileRepository fileRepository) {
        this.userRepository = userRepository;
        this.fileRepository = fileRepository;
        this.fileStorageLocation = Paths.get("file-storage").toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (IOException e) {
            throw new RuntimeException("Failed to create directory for file storage.");
        }
    }

    public Page<File> getUserFiles(Integer userId, int page, int size) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found."));
        Pageable pageable = PageRequest.of(page, size);
        return fileRepository.findByUser(user, pageable);
    }

    public void uploadFile(Integer userId, MultipartFile file) throws IOException {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found."));

        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));

        // Проверка, существует ли файл с таким именем
        if (fileRepository.findByNameAndUser(fileName, user).isPresent()) {
            throw new IllegalArgumentException("File with the same name already exists.");
        }

        Path targetLocation = this.fileStorageLocation.resolve(fileName);
        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

        File newFile = new File(fileName, user);
        fileRepository.save(newFile);
    }


    public Resource downloadFile(Integer userId, Integer fileId) throws FileNotFoundException {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found."));
        File file = fileRepository.findByIdAndUser(fileId, user)
                .orElseThrow(FileNotFoundException::new);

        Path filePath = this.fileStorageLocation.resolve(file.getName()).normalize();
        Resource resource;
        try {
            resource = new UrlResource(filePath.toUri());
        } catch (MalformedURLException e) {
            throw new FileNotFoundException("File not found.");
        }

        if (resource.exists()) {
            return resource;
        } else {
            throw new FileNotFoundException("File not found.");
        }
    }

    public boolean deleteFile(Integer userId, Integer fileId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found."));
        File file = fileRepository.findByIdAndUser(fileId, user)
                .orElseThrow(() -> new IllegalArgumentException("File not found."));

        try {
            Files.deleteIfExists(this.fileStorageLocation.resolve(file.getName()));
        } catch (IOException e) {
            return false;
        }

        fileRepository.delete(file);
        return true;
    }

    public boolean renameFile(Integer userId, Integer fileId, String newName) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found."));
        File file = fileRepository.findByIdAndUser(fileId, user)
                .orElseThrow(() -> new IllegalArgumentException("File not found."));

        try {
            Path sourcePath = this.fileStorageLocation.resolve(file.getName());
            Path targetPath = this.fileStorageLocation.resolve(newName);
            Files.move(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);

            file.setName(newName);
            fileRepository.save(file);

            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public String getContentType(String extension) {
        return switch (extension.toLowerCase()) {
            case "jpg", "jpeg" -> "image/jpeg";
            case "png" -> "image/png";
            case "gif" -> "image/gif";
            case "pdf" -> "application/pdf";
            case "txt" -> "text/plain";
            case "doc", "docx" -> "application/msword";
            case "xls", "xlsx" -> "application/vnd.ms-excel";
            case "zip" -> "application/zip";
            case "rar" -> "application/x-rar-compressed";
            case "7z" -> "application/x-7z-compressed";
            case "mp4" -> "video/mp4";
            case "avi" -> "video/x-msvideo";
            case "mp3" -> "audio/mpeg";
            case "wav" -> "audio/wav";
            // Add more cases for other possible extensions and content types
            default -> "application/octet-stream";
        };
    }
}
