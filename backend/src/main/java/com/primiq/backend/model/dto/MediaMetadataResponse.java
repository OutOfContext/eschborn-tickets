package com.primiq.backend.model.dto;

import java.time.Instant;

public record MediaMetadataResponse(
  String id,
  String originalFilename,
  String contentType,
  long sizeBytes,
  Instant createdAt,
  String contentUrl,
  String embed
) {
}

