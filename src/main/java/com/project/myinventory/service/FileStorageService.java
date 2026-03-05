package com.project.myinventory.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileStorageService {
    public String storeFile(MultipartFile file, String directory) throws IOException {
        if (file == null || file.isEmpty()) return null;

        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path uploadPath = Paths.get("uploads/" + directory);

        if (!Files.exists(uploadPath)) Files.createDirectories(uploadPath);

        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, uploadPath.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
        }
        return fileName;
    }
}
