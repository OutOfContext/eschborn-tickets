package com.primiq.backend.config;

import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "media.storage")
public record MediaStorageProperties(
  @NotBlank String root
) {
}

