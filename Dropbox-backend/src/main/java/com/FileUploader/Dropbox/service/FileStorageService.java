package com.FileUploader.Dropbox.service;

import com.FileUploader.Dropbox.model.FileMetadata;
import com.FileUploader.Dropbox.repository.FileMetadataRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class FileStorageService {

    private final Path root = Paths.get("uploads");

    @Autowired
    private FileMetadataRepository repo;

    private static final List<String> SUPPORTED_FILE_TYPES = List.of(
            "image/png", "image/jpeg", "application/json", "text/plain"
    );

    @PostConstruct
    public void init() {
        try {
            Files.createDirectories(root);
            log.info("File upload root directory created at: {}", root.toAbsolutePath());
        } catch (IOException e) {
            log.error("Could not create upload directory", e);
            throw new RuntimeException("Failed to initialize upload directory", e);
        }
    }

    public FileMetadata saveFile(MultipartFile file) {
        try {
            validateFile(file);

            String originalName = file.getOriginalFilename();
            String storedFilename = UUID.randomUUID() + "_" + originalName;
            Path destination = root.resolve(storedFilename);

            Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
            log.info("File saved: {}", storedFilename);
            FileMetadata meta = new FileMetadata();
            meta.setFilename(originalName);
            meta.setFiletype(file.getContentType());
            meta.setFileSize(file.getSize());
            meta.setUploadTime(LocalDateTime.now());
            meta.setFilePath(destination.toString());

            return repo.save(meta);

        } catch (IOException e) {
            log.error("Error saving file: {}", e.getMessage());
            throw new RuntimeException("Could not store the file. Please try again.", e);
        }
    }

    public ResponseEntity<Resource> downloadFile(Long id) throws FileNotFoundException {
        FileMetadata meta = repo.findById(id)
                .orElseThrow(() -> new FileNotFoundException("File with ID " + id + " not found."));

        try {
            Path filePath = Paths.get(meta.getFilePath()).toAbsolutePath().normalize();
            UrlResource resource = new UrlResource(filePath.toUri());

            if (!resource.exists()) {
                throw new FileNotFoundException("File not found on disk.");
            }
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + meta.getFilename() + "\"")
                    .header(HttpHeaders.CONTENT_TYPE, meta.getFiletype())
                    .body((Resource) resource);

        } catch (MalformedURLException e) {
            log.error("Invalid file URL: {}", e.getMessage());
            throw new RuntimeException("Failed to download file due to malformed path.", e);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    public List<FileMetadata> getAllFiles() {
        return repo.findAll();
    }

    private void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Uploaded file is empty.");
        }

        String fileType = file.getContentType();
        if (!SUPPORTED_FILE_TYPES.contains(fileType)) {
            throw new IllegalArgumentException("Unsupported file type: " + fileType);
        }
    }
}