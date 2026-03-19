package com.primiq.backend.model.dto;

import java.util.Set;

public record CurrentUserResponse(
  String sessionId,
  String username,
  Set<String> roles
) {
}

