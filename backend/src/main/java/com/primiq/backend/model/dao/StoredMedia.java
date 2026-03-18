package com.primiq.backend.model.dao;

import java.time.Instant;

public record StoredMedia(
  String id,
  String originalFilename,
  String contentType,
  long sizeBytes,
  Instant createdAt
) {
}

