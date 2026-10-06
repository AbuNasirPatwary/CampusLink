package com.campuslink.media;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/media")
public class MediaController {
    private final FileStorageService fileStorageService;

    public MediaController(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    @PostMapping("/upload")
    public FileUploadResponse upload(@RequestParam MultipartFile file,
                                     @RequestParam String folder) {
        return new FileUploadResponse(fileStorageService.store(file, folder));
    }

    @GetMapping("/private/{folder}/{filename:.+}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Resource> privateMedia(@PathVariable String folder,
                                                  @PathVariable String filename) {
        Resource resource = fileStorageService.loadPrivate(folder, filename);
        MediaType mediaType;
        try {
            mediaType = MediaType.parseMediaType(fileStorageService.contentType(resource));
        } catch (Exception ignored) {
            mediaType = MediaType.APPLICATION_OCTET_STREAM;
        }
        return ResponseEntity.ok()
                .header(HttpHeaders.CACHE_CONTROL, "no-store")
                .contentType(mediaType)
                .body(resource);
    }

    public record FileUploadResponse(String url) { }
}
