package com.primiq.backend.service;

import com.primiq.backend.config.KeycloakBootstrapProperties;
import com.primiq.backend.model.dao.AppUser;
import com.primiq.backend.model.dao.base.AbstractUserProfile;
import com.primiq.backend.model.repository.AppUserRepository;
import java.time.Instant;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@ConditionalOnProperty(name = "app.security.enabled", havingValue = "true", matchIfMissing = true)
public class UserRoleSyncService {

  private static final Logger log = LoggerFactory.getLogger(UserRoleSyncService.class);

  private final AppUserRepository appUserRepository;
  private final KeycloakProvisioningService keycloakProvisioningService;
  private final KeycloakBootstrapProperties properties;

  private final Map<String, CacheEntry> roleCache = new ConcurrentHashMap<>();

  public UserRoleSyncService(AppUserRepository appUserRepository,
                             KeycloakProvisioningService keycloakProvisioningService,
                             KeycloakBootstrapProperties properties) {
    this.appUserRepository = appUserRepository;
    this.keycloakProvisioningService = keycloakProvisioningService;
    this.properties = properties;
  }

  public Set<String> getRolesForUser(String referenceId) {
    if (!properties.roleCacheEnabled()) {
      return keycloakProvisioningService.getUserRealmRolesByReferenceId(referenceId);
    }

    CacheEntry current = roleCache.get(referenceId);
    if (current != null && current.expiresAt().isAfter(Instant.now())) {
      return current.roles();
    }

    return refreshSingleUserFromKeycloak(referenceId);
  }

  @Transactional
  public Set<String> refreshSingleUserFromKeycloak(String referenceId) {
    Set<String> roles = keycloakProvisioningService.getUserRealmRolesByReferenceId(referenceId);

    appUserRepository.findByReferenceId(referenceId).ifPresentOrElse(user -> {
      user.setRoles(roles);
      appUserRepository.save(user);
    }, () -> {
      AppUser newUser = new AppUser();
      newUser.setReferenceId(referenceId);
      newUser.setDisplayName(referenceId);
      newUser.setUsername(referenceId);
      newUser.setEnabled(true);
      newUser.setRoles(roles);
      appUserRepository.save(newUser);
    });

    roleCache.put(referenceId, new CacheEntry(roles, Instant.now().plusSeconds(properties.roleCacheTtlSeconds())));
    return roles;
  }

  @Transactional
  public void triggerOnDemandResync() {
    refreshAllUsersFromKeycloak();
  }

  @Scheduled(fixedDelayString = "${app.keycloak.resync-interval-ms:60000}")
  @Transactional
  public void refreshAllUsersFromKeycloak() {
    try {
      for (KeycloakProvisioningService.KeycloakUserSnapshot snapshot : keycloakProvisioningService.fetchAllUsersWithRealmRoles()) {
        AppUser user = appUserRepository.findByReferenceId(snapshot.referenceId()).orElseGet(AppUser::new);
        user.setReferenceId(snapshot.referenceId());
        user.setUsername(snapshot.username());
        user.setDisplayName(snapshot.displayName() == null ? snapshot.username() : snapshot.displayName());
        user.setEnabled(snapshot.enabled());
        user.setRoles(snapshot.roles());

        AbstractUserProfile profile = user.getUserProfile();
        if (profile == null) {
          profile = new AbstractUserProfile();
        }
        profile.setEmail(snapshot.email());
        user.setUserProfile(profile);

        appUserRepository.save(user);
        roleCache.put(snapshot.referenceId(),
          new CacheEntry(snapshot.roles(), Instant.now().plusSeconds(properties.roleCacheTtlSeconds())));
      }
    } catch (RuntimeException ex) {
      log.warn("Skipping Keycloak user-role refresh cycle due to error: {}", ex.getMessage());
    }
  }

  public void invalidateRoleCache(String referenceId) {
    roleCache.remove(referenceId);
  }

  private record CacheEntry(Set<String> roles, Instant expiresAt) {
  }
}

