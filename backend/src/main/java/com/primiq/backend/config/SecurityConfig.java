package com.primiq.backend.config;

import com.primiq.backend.service.UserRoleSyncService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@ConditionalOnProperty(name = "app.security.enabled", havingValue = "true", matchIfMissing = false)
public class SecurityConfig {

  @Bean
  SecurityFilterChain securityFilterChain(HttpSecurity http, UserRoleSyncService userRoleSyncService) throws Exception {
    http
      .csrf(csrf -> csrf.disable())
      .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
      .authorizeHttpRequests(auth -> auth
        .requestMatchers("/api/auth/login", "/api/auth/logout").permitAll()
        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
        .anyRequest().authenticated()
      )
      .oauth2ResourceServer(oauth2 -> oauth2
        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
        .authenticationEntryPoint((request, response, authException) -> {
          tryRefreshRoles(userRoleSyncService, request);
          response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());
        })
        .accessDeniedHandler((request, response, accessDeniedException) -> {
          tryRefreshRoles(userRoleSyncService, request);
          response.sendError(HttpServletResponse.SC_FORBIDDEN, accessDeniedException.getMessage());
        })
      )
      .httpBasic(Customizer.withDefaults());

    return http.build();
  }

  private JwtAuthenticationConverter jwtAuthenticationConverter() {
    JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
    converter.setJwtGrantedAuthoritiesConverter(this::extractRealmRoles);
    return converter;
  }

  @SuppressWarnings("unchecked")
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

  private void tryRefreshRoles(UserRoleSyncService userRoleSyncService, HttpServletRequest request) {
    String userReferenceId = request.getUserPrincipal() == null ? null : request.getUserPrincipal().getName();
    try {
      if (userReferenceId == null || userReferenceId.isBlank()) {
        userRoleSyncService.triggerOnDemandResync();
      } else {
        userRoleSyncService.refreshSingleUserFromKeycloak(userReferenceId);
      }
    } catch (RuntimeException ignored) {
      // Auth failure handling should not fail request processing.
    }
  }
}

