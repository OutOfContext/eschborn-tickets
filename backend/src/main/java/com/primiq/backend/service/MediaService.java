package com.primiq.backend.service;

import tools.jackson.databind.ObjectMapper;
import com.primiq.backend.config.MediaStorageProperties;
import com.primiq.backend.model.dao.StoredMedia;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.Instant;
import java.util.UUID;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class MediaService {

  private final Path storageRoot;
  private final ObjectMapper objectMapper;

  public MediaService(MediaStorageProperties storageProperties, ObjectMapper objectMapper) {
    this.storageRoot = Path.of(storageProperties.root()).toAbsolutePath().normalize();
    this.objectMapper = objectMapper;

    try {
      Files.createDirectories(this.storageRoot);
    } catch (IOException ex) {
      throw new IllegalStateException("Media-Speicherverzeichnis konnte nicht erstellt werden", ex);
    }
  }

  public StoredMedia store(MultipartFile multipartFile) {
    if (multipartFile == null || multipartFile.isEmpty()) {
      throw new IllegalArgumentException("Die Datei ist leer oder fehlt");
    }

    String id = UUID.randomUUID().toString();
    String originalFilename = sanitizeFilename(multipartFile.getOriginalFilename());
    String contentType = normalizeContentType(multipartFile.getContentType());

    Path binaryPath = filePath(id);
    Path metadataPath = metadataPath(id);

    try (InputStream inputStream = multipartFile.getInputStream()) {
      Files.copy(inputStream, binaryPath, StandardCopyOption.REPLACE_EXISTING);

      StoredMedia storedMedia = new StoredMedia(
        id,
        originalFilename,
        contentType,
        multipartFile.getSize(),
        Instant.now()
      );

      objectMapper.writeValue(metadataPath.toFile(), storedMedia);
      return storedMedia;
    } catch (IOException ex) {
      throw new IllegalStateException("Datei konnte nicht gespeichert werden", ex);
    }
  }

  public StoredMedia getMetadata(String id) {
    Path metadataPath = metadataPath(id);
    if (!Files.exists(metadataPath)) {
      throw new IllegalArgumentException("Media nicht gefunden");
    }

    return objectMapper.readValue(metadataPath.toFile(), StoredMedia.class);
  }

  public Resource getContent(String id) {
    Path path = filePath(id);
    if (!Files.exists(path)) {
      throw new IllegalArgumentException("Media nicht gefunden");
    }

    return new FileSystemResource(path);
  }

  private Path filePath(String id) {
    return storageRoot.resolve(id + ".bin");
  }

  private Path metadataPath(String id) {
    return storageRoot.resolve(id + ".json");
  }

  private String sanitizeFilename(String originalFilename) {
    String cleaned = StringUtils.cleanPath(originalFilename == null ? "file" : originalFilename);
    Path fileNameOnly = Path.of(cleaned).getFileName();
    return fileNameOnly == null ? "file" : fileNameOnly.toString();
  }

  private String normalizeContentType(String contentType) {
    if (!StringUtils.hasText(contentType)) {
      return MediaType.APPLICATION_OCTET_STREAM_VALUE;
    }
    return contentType;
  }
}
