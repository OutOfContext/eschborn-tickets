package com.primiq.backend.controller;

import com.primiq.backend.model.dto.LoginRequest;
import com.primiq.backend.model.dto.LoginResponse;
import com.primiq.backend.model.dto.CurrentUserResponse;
import com.primiq.backend.service.KeycloakPasswordGrantService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@ConditionalOnProperty(name = "app.security.enabled", havingValue = "true", matchIfMissing = false)
public class LoginController {

  public static final String SESSION_ACCESS_TOKEN = "SESSION_ACCESS_TOKEN";
  public static final String SESSION_REFRESH_TOKEN = "SESSION_REFRESH_TOKEN";

  private final KeycloakPasswordGrantService passwordGrantService;
  private final JwtDecoder jwtDecoder;

  public LoginController(KeycloakPasswordGrantService passwordGrantService, JwtDecoder jwtDecoder) {
    this.passwordGrantService = passwordGrantService;
    this.jwtDecoder = jwtDecoder;
  }

  @PostMapping("/login")
  public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request,
                                             HttpServletRequest httpServletRequest) {
    KeycloakPasswordGrantService.TokenResponse tokenResponse =
      passwordGrantService.authenticate(request.username(), request.password());

    if (tokenResponse.accessToken() == null || tokenResponse.accessToken().isBlank()) {
      throw new BadCredentialsException("Invalid credentials");
    }

    Jwt jwt = jwtDecoder.decode(tokenResponse.accessToken());
    Collection<GrantedAuthority> authorities = extractRealmRoles(jwt);

    String principalName = jwt.getClaimAsString("preferred_username");
    if (principalName == null || principalName.isBlank()) {
      principalName = request.username();
    }

    JwtAuthenticationToken authentication = new JwtAuthenticationToken(jwt, authorities, principalName);
    SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
    securityContext.setAuthentication(authentication);
    SecurityContextHolder.setContext(securityContext);

    var session = httpServletRequest.getSession(true);
    session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, securityContext);
    session.setAttribute(SESSION_ACCESS_TOKEN, tokenResponse.accessToken());
    session.setAttribute(SESSION_REFRESH_TOKEN, tokenResponse.refreshToken());

    Set<String> roles = authorities.stream()
      .map(GrantedAuthority::getAuthority)
      .map(this::stripRolePrefix)
      .collect(Collectors.toSet());

    return ResponseEntity.ok(new LoginResponse(
      session.getId(),
      principalName,
      roles,
      tokenResponse.tokenType(),
      tokenResponse.expiresIn()
    ));
  }

  @GetMapping("/me")
  public ResponseEntity<CurrentUserResponse> me(HttpServletRequest httpServletRequest) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || !authentication.isAuthenticated()) {
      throw new InsufficientAuthenticationException("Not authenticated");
    }

    Set<String> roles = authentication.getAuthorities().stream()
      .map(GrantedAuthority::getAuthority)
      .map(this::stripRolePrefix)
      .collect(Collectors.toSet());

    String sessionId = httpServletRequest.getSession(false) == null
      ? null
      : httpServletRequest.getSession(false).getId();

    return ResponseEntity.ok(new CurrentUserResponse(sessionId, authentication.getName(), roles));
  }

  @DeleteMapping("/logout")
  public ResponseEntity<Void> logout(HttpServletRequest httpServletRequest) {
    var session = httpServletRequest.getSession(false);
    if (session != null) {
      session.invalidate();
    }
    SecurityContextHolder.clearContext();
    return ResponseEntity.noContent().build();
  }

  private Collection<GrantedAuthority> extractRealmRoles(Jwt jwt) {
    Object realmAccessClaim = jwt.getClaim("realm_access");
    if (!(realmAccessClaim instanceof Map<?, ?> realmAccess)) {
      return List.of();
    }

    Object rolesClaim = realmAccess.get("roles");
    if (!(rolesClaim instanceof List<?> roles)) {
      return List.of();
    }

    List<GrantedAuthority> authorities = new ArrayList<>();
    for (Object role : roles) {
      if (role instanceof String roleName && !roleName.isBlank()) {
        authorities.add(new SimpleGrantedAuthority("ROLE_" + roleName));
      }
    }
    return authorities;
  }

  private String stripRolePrefix(String role) {
    return role != null && role.startsWith("ROLE_") ? role.substring(5) : role;
  }
}

