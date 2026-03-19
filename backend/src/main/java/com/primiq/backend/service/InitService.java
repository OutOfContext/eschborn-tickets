package com.primiq.backend.service;

import com.primiq.backend.config.KeycloakBootstrapProperties;
import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(name = "app.keycloak.bootstrap-enabled", havingValue = "true", matchIfMissing = true)
public class InitService implements ApplicationListener<ApplicationReadyEvent> {

  private static final Logger log = LoggerFactory.getLogger(InitService.class);

  private final KeycloakBootstrapProperties properties;
  private final KeycloakProvisioningService keycloakProvisioningService;
  private final UserRoleSyncService userRoleSyncService;
  private final Environment environment;

  public InitService(KeycloakBootstrapProperties properties,
                     KeycloakProvisioningService keycloakProvisioningService,
                     UserRoleSyncService userRoleSyncService,
                     Environment environment) {
    this.properties = properties;
    this.keycloakProvisioningService = keycloakProvisioningService;
    this.userRoleSyncService = userRoleSyncService;
    this.environment = environment;
  }

  @Override
  public void onApplicationEvent(ApplicationReadyEvent event) {
    if (!properties.bootstrapEnabled()) {
      return;
    }

    try {
      keycloakProvisioningService.initializeRealmAndRoles();
      if (isDevProfileActive()) {
        keycloakProvisioningService.initializeDevUsers();
      }
    } catch (RuntimeException ex) {
      log.warn("Keycloak bootstrap skipped due to error: {}", ex.getMessage());
    }

    try {
      userRoleSyncService.refreshAllUsersFromKeycloak();
    } catch (RuntimeException ex) {
      log.warn("Initial Keycloak user-role sync skipped due to error: {}", ex.getMessage());
    }
  }

  private boolean isDevProfileActive() {
    return Arrays.asList(environment.getActiveProfiles()).contains("dev");
  }
}
