package com.FileUploader.Dropbox.controller;
import com.FileUploader.Dropbox.model.FileMetadata;
import com.FileUploader.Dropbox.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileController {

    private final FileStorageService fileStorageService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "tags", required = false) String tags
    ) throws IOException {
        log.info("Upload request received for file: {}, description: {}, tags: {}", file.getOriginalFilename(), description, tags);
        fileStorageService.saveFile(file);
        log.info("File uploaded successfully: {}", file.getOriginalFilename());
        return ResponseEntity.status(HttpStatus.CREATED).body("File uploaded successfully.");
    }

    @GetMapping
    public  ResponseEntity<List<FileMetadata>> listFiles() {
        log.info("Listing all uploaded files.");
        var files = fileStorageService.getAllFiles();
        log.debug("Retrieved {} files from storage.", files.size());
        return ResponseEntity.ok(files);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long id) throws FileNotFoundException {
        log.info("Download request received for file ID: {}", id);
        ResponseEntity<Resource> response = fileStorageService.downloadFile(id);
        log.info("File with ID {} downloaded successfully.", id);
        return response;
    }
}
