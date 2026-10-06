package com.campuslink.media;

import com.campuslink.common.exception.ApiException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Set;
import java.util.UUID;

@Service
public class FileStorageService {
    private static final Set<String> PUBLIC_FOLDERS = Set.of("profiles", "findit", "borrowhub", "complaints", "talent");
    private static final String PRIVATE_FINDIT_FOLDER = "findit-private";

    @Value("${app.upload-dir}")
    private String uploadDir;

    @Value("${app.private-upload-dir:private-uploads}")
    private String privateUploadDir;

    public String store(MultipartFile file, String folder) {
        validateFile(file);
        String original = file.getOriginalFilename() == null ? "file" : file.getOriginalFilename();
        String extension = original.contains(".") ? original.substring(original.lastIndexOf('.')) : "";
        String safeName = UUID.randomUUID() + extension.toLowerCase();

        if (PRIVATE_FINDIT_FOLDER.equals(folder)) {
            save(file, Paths.get(privateUploadDir, "findit"), safeName);
            // Internal key only. It is never exposed by normal FindIt response DTOs.
            return "findit/" + safeName;
        }

        if (!PUBLIC_FOLDERS.contains(folder)) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Invalid upload folder.");
        }
        save(file, Paths.get(uploadDir, folder), safeName);
        return "/uploads/" + folder + "/" + safeName;
    }

    public Resource loadPrivate(String folder, String filename) {
        if (!"findit".equals(folder)) {
            throw new ApiException(HttpStatus.NOT_FOUND, "Private media not found.");
        }
        try {
            Path root = Paths.get(privateUploadDir).toAbsolutePath().normalize();
            Path file = root.resolve(folder).resolve(filename).normalize();
            if (!file.startsWith(root) || !Files.exists(file) || !Files.isRegularFile(file)) {
                throw new ApiException(HttpStatus.NOT_FOUND, "Private media not found.");
            }
            Resource resource = new UrlResource(file.toUri());
            if (!resource.isReadable()) {
                throw new ApiException(HttpStatus.NOT_FOUND, "Private media not found.");
            }
            return resource;
        } catch (MalformedURLException ex) {
            throw new ApiException(HttpStatus.NOT_FOUND, "Private media not found.");
        }
    }

    public String contentType(Resource resource) {
        try {
            String type = Files.probeContentType(resource.getFile().toPath());
            return type == null ? "application/octet-stream" : type;
        } catch (IOException ex) {
            return "application/octet-stream";
        }
    }

    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Please choose a file to upload.");
        }
        String contentType = file.getContentType();
        if (contentType != null && !contentType.startsWith("image/") && !contentType.startsWith("video/")) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Only image and video uploads are supported.");
        }
    }

    private void save(MultipartFile file, Path directory, String safeName) {
        try {
            Path targetDir = directory.toAbsolutePath().normalize();
            Files.createDirectories(targetDir);
            Path target = targetDir.resolve(safeName).normalize();
            if (!target.startsWith(targetDir)) {
                throw new ApiException(HttpStatus.BAD_REQUEST, "Invalid file path.");
            }
            Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Could not save uploaded file.");
        }
    }
}
