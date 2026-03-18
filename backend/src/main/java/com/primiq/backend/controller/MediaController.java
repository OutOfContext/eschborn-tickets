package com.primiq.backend.controller;

import com.primiq.backend.model.dao.StoredMedia;
import com.primiq.backend.model.dto.MediaMetadataResponse;
import com.primiq.backend.service.MediaService;
import jakarta.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/media")
public class MediaController {

  private final MediaService mediaService;

  public MediaController(MediaService mediaService) {
    this.mediaService = mediaService;
  }

  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<MediaMetadataResponse> upload(@RequestParam("file") MultipartFile file,
                                                      HttpServletRequest request) {
    StoredMedia storedMedia = mediaService.store(file);
    MediaMetadataResponse body = toResponse(storedMedia, request);
    return ResponseEntity.status(HttpStatus.CREATED).body(body);
  }

  @GetMapping("/{id}")
  public MediaMetadataResponse metadata(@PathVariable String id, HttpServletRequest request) {
    return toResponse(mediaService.getMetadata(id), request);
  }

  @GetMapping("/{id}/content")
  public ResponseEntity<Resource> content(@PathVariable String id) {
    StoredMedia metadata = mediaService.getMetadata(id);
    Resource resource = mediaService.getContent(id);

    MediaType mediaType = parseMediaType(metadata.contentType());

    return ResponseEntity.ok()
      .contentType(mediaType)
      .contentLength(metadata.sizeBytes())
      .header(HttpHeaders.CONTENT_DISPOSITION,
        ContentDisposition.inline().filename(metadata.originalFilename(), StandardCharsets.UTF_8).build().toString())
      .body(resource);
  }

  private MediaMetadataResponse toResponse(StoredMedia media, HttpServletRequest request) {
    String contentUrl = ServletUriComponentsBuilder.fromRequestUri(request)
      .replacePath("/api/media/" + media.id() + "/content")
      .replaceQuery(null)
      .build()
      .toUriString();

    String embed = isImage(media.contentType())
      ? "![" + media.originalFilename() + "](" + contentUrl + ")"
      : "[" + media.originalFilename() + "](" + contentUrl + ")";

    return new MediaMetadataResponse(
      media.id(),
      media.originalFilename(),
      media.contentType(),
      media.sizeBytes(),
      media.createdAt(),
      contentUrl,
      embed
    );
  }

  private MediaType parseMediaType(String value) {
    try {
      return MediaType.parseMediaType(value);
    } catch (IllegalArgumentException ex) {
      return MediaType.APPLICATION_OCTET_STREAM;
    }
  }

  private boolean isImage(String contentType) {
    return MimeTypeUtils.parseMimeType(contentType).getType().equalsIgnoreCase("image");
  }
}

