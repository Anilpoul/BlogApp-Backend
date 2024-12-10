package com.blogapp.services.impl;

import com.blogapp.services.FileService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {
    @Override
    public String uploadImage(String path, MultipartFile file) throws IOException {
        // Validate file type
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("Only image files are allowed to upload!");
        }

        // File Name
        String name = file.getOriginalFilename();

        // Generate random unique identifier for the file
        String randomID = UUID.randomUUID().toString();
        String fileExtension = name.substring(name.lastIndexOf("."));
        String fileName = randomID.concat(fileExtension);

        // Full Path
        String filePath = path + File.separator + fileName;

        // Create folder if it doesn't exist
        File f = new File(path);
        if (!f.exists()) {
            f.mkdir();
        }

        // File Copy
        Files.copy(file.getInputStream(), Paths.get(filePath));
        return fileName;
    }

    @Override
    public InputStream getResource(String path, String fileName) throws FileNotFoundException {
        String fullPath = path+ File.separator+ fileName;
        InputStream is = new FileInputStream(fullPath);

        // Db logic to return inputStream

        return is;
    }
}
