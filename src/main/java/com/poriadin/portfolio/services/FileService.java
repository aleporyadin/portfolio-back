package com.poriadin.portfolio.services;

import com.poriadin.portfolio.entities.File;
import com.poriadin.portfolio.entities.User;
import com.poriadin.portfolio.repositories.FileRepository;
import com.poriadin.portfolio.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
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
import java.util.List;

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

    public List<File> getUserFiles(Integer userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found."));
        return fileRepository.findByUser(user);
    }

    public void uploadFile(Integer userId, MultipartFile file) throws IOException {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found."));

        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
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
}
