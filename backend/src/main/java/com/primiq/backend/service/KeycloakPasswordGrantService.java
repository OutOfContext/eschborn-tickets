package com.primiq.backend.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.primiq.backend.config.KeycloakBootstrapProperties;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

@Service
@ConditionalOnProperty(name = "app.security.enabled", havingValue = "true", matchIfMissing = false)
public class KeycloakPasswordGrantService {

  private final KeycloakBootstrapProperties properties;
  private final ObjectMapper objectMapper;
  private final HttpClient httpClient;

  public KeycloakPasswordGrantService(KeycloakBootstrapProperties properties,
      ObjectMapper objectMapper) {
    this.properties = properties;
    this.objectMapper = objectMapper;
    this.httpClient = HttpClient.newHttpClient();
  }

  public TokenResponse authenticate(String username, String password) {
    String formBody = buildFormBody(username, password);
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(tokenEndpoint()))
        .header("Content-Type", "application/x-www-form-urlencoded")
        .POST(HttpRequest.BodyPublishers.ofString(formBody))
        .build();

    try {
      HttpResponse<String> response = httpClient.send(request,
          HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
      if (response.statusCode() < 200 || response.statusCode() >= 300) {
        throw new IllegalArgumentException(
            "Keycloak authentication failed with status " + response.statusCode());
      }

      return objectMapper.readValue(response.body(), TokenResponse.class);
    } catch (IOException ex) {
      throw new IllegalStateException("Could not parse Keycloak token response", ex);
    } catch (InterruptedException ex) {
      Thread.currentThread().interrupt();
      throw new IllegalStateException("Keycloak authentication request interrupted", ex);
    }
  }

  private String tokenEndpoint() {
    return properties.serverUrl() + "/realms/" + properties.realm()
        + "/protocol/openid-connect/token";
  }

  private String buildFormBody(String username, String password) {
    return toFormValue(Map.of(
        "grant_type", "password",
        "client_id", properties.backendClientId(),
        "username", username,
        "password", password
    ));
  }

  private String toFormValue(Map<String, String> values) {
    return values.entrySet().stream()
        .map(entry -> urlEncode(entry.getKey()) + "=" + urlEncode(entry.getValue()))
        .reduce((left, right) -> left + "&" + right)
        .orElse("");
  }

  private String urlEncode(String value) {
    return URLEncoder.encode(value, StandardCharsets.UTF_8);
  }

  public record TokenResponse(
      @JsonProperty("access_token") String accessToken,
      @JsonProperty("refresh_token") String refreshToken,
      @JsonProperty("token_type") String tokenType,
      @JsonProperty("expires_in") long expiresIn
  ) {
  }
}

