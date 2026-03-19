package com.primiq.backend.model.dto;

import java.util.Set;

public record LoginResponse(
  String sessionId,
  String username,
  Set<String> roles,
  String tokenType,
  long expiresIn
) {
}

