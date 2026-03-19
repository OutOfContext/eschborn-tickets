package com.primiq.backend.config;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app.keycloak")
public record KeycloakBootstrapProperties(
  boolean bootstrapEnabled,
  boolean roleCacheEnabled,
  long roleCacheTtlSeconds,
  long resyncIntervalMs,
  @NotBlank String serverUrl,
  @NotBlank String adminRealm,
  @NotBlank String adminUsername,
  @NotBlank String adminPassword,
  @NotBlank String realm,
  @NotBlank String backendClientId,
  @NotEmpty List<String> baseRoles,
  @NotEmpty List<String> devUsers
) {
}

