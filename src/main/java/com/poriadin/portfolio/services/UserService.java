package com.poriadin.portfolio.services;

import org.apache.commons.io.IOUtils;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class UserService {
    public Resource loadAvatarResource(String avatarPath) {

        try {
            Path avatarLocation = Paths.get("avatars").resolve(avatarPath);
            Resource resource = new FileSystemResource(avatarLocation.toAbsolutePath().toString());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Avatar file not found");
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to load avatar file", e);
        }
    }

    public byte[] loadAvatarBytes(String avatarPath) {
        try {
            Resource avatarResource = loadAvatarResource(avatarPath);
            InputStream inputStream = avatarResource.getInputStream();
            return IOUtils.toByteArray(inputStream);
        } catch (IOException e) {
            // Handle the exception or log an error
            return null;
        }
    }

}
