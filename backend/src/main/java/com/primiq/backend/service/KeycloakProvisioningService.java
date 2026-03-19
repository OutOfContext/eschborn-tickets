package com.primiq.backend.service;

import com.primiq.backend.config.KeycloakBootstrapProperties;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RealmRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class KeycloakProvisioningService {

  private static final Logger log = LoggerFactory.getLogger(KeycloakProvisioningService.class);

  private final KeycloakBootstrapProperties properties;
  private final Keycloak keycloak;

  public KeycloakProvisioningService(KeycloakBootstrapProperties properties) {
    this.properties = properties;
    this.keycloak = KeycloakBuilder.builder()
      .serverUrl(properties.serverUrl())
      .realm(properties.adminRealm())
      .clientId("admin-cli")
      .username(properties.adminUsername())
      .password(properties.adminPassword())
      .build();
  }

  public void initializeRealmAndRoles() {
    ensureRealmExists();
    RealmResource realmResource = realm();
    ensureBackendClientExists(realmResource);
    for (String roleName : properties.baseRoles()) {
      ensureRoleExists(realmResource, roleName);
    }
  }

  public void initializeDevUsers() {
    RealmResource realmResource = realm();
    Map<String, String> userRoleMap = Map.of(
      "admin", "admin",
      "dev", "employee",
      "project", "manager"
    );

    for (String username : properties.devUsers()) {
      String roleName = userRoleMap.getOrDefault(username, "employee");
      String userId = ensureUserExists(realmResource, username, username + "123");
      assignRealmRoles(realmResource, userId, Set.of(roleName));
    }
  }

  public Set<String> getUserRealmRolesByReferenceId(String referenceId) {
    try {
      return realm()
        .users()
        .get(referenceId)
        .roles()
        .realmLevel()
        .listAll()
        .stream()
        .map(RoleRepresentation::getName)
        .filter(Objects::nonNull)
        .collect(Collectors.toSet());
    } catch (NotFoundException ex) {
      return Set.of();
    } catch (RuntimeException ex) {
      log.warn("Could not fetch realm roles for user {} from Keycloak: {}", referenceId, ex.getMessage());
      return Set.of();
    }
  }

  public List<KeycloakUserSnapshot> fetchAllUsersWithRealmRoles() {
    RealmResource realmResource = realm();
    final List<UserRepresentation> users;
    try {
      users = realmResource.users().list();
    } catch (NotFoundException ex) {
      log.warn("Keycloak realm {} not found while fetching users.", properties.realm());
      return List.of();
    } catch (RuntimeException ex) {
      log.warn("Could not fetch users from Keycloak realm {}: {}", properties.realm(), ex.getMessage());
      return List.of();
    }

    List<KeycloakUserSnapshot> snapshots = new ArrayList<>();

    for (UserRepresentation user : users) {
      if (user.getId() == null || user.getUsername() == null) {
        continue;
      }

      Set<String> roles;
      try {
        roles = realmResource.users().get(user.getId()).roles().realmLevel().listAll()
          .stream()
          .map(RoleRepresentation::getName)
          .filter(Objects::nonNull)
          .collect(Collectors.toSet());
      } catch (RuntimeException ex) {
        log.warn("Could not fetch roles for Keycloak user {} ({}): {}", user.getUsername(), user.getId(), ex.getMessage());
        roles = Set.of();
      }

      snapshots.add(new KeycloakUserSnapshot(
        user.getId(),
        user.getUsername(),
        user.getFirstName(),
        user.getEmail(),
        Boolean.TRUE.equals(user.isEnabled()),
        roles
      ));
    }

    return snapshots;
  }

  private RealmResource realm() {
    return keycloak.realm(properties.realm());
  }

  private void ensureRealmExists() {
    try {
      realm().toRepresentation();
      return;
    } catch (NotFoundException ex) {
      // Realm does not exist yet, create it below.
    }

    RealmRepresentation representation = new RealmRepresentation();
    representation.setRealm(properties.realm());
    representation.setEnabled(true);
    keycloak.realms().create(representation);
  }

  private void ensureRoleExists(RealmResource realmResource, String roleName) {
    boolean roleExists = realmResource.roles().list().stream()
      .anyMatch(role -> roleName.equals(role.getName()));

    if (!roleExists) {
      RoleRepresentation roleRepresentation = new RoleRepresentation();
      roleRepresentation.setName(roleName);
      realmResource.roles().create(roleRepresentation);
    }
  }

  private void ensureBackendClientExists(RealmResource realmResource) {
    boolean exists = !realmResource.clients().findByClientId(properties.backendClientId()).isEmpty();
    if (exists) {
      return;
    }

    ClientRepresentation clientRepresentation = new ClientRepresentation();
    clientRepresentation.setClientId(properties.backendClientId());
    clientRepresentation.setName(properties.backendClientId());
    clientRepresentation.setEnabled(true);
    clientRepresentation.setPublicClient(true);
    clientRepresentation.setDirectAccessGrantsEnabled(true);
    clientRepresentation.setStandardFlowEnabled(true);
    realmResource.clients().create(clientRepresentation);
  }

  private String ensureUserExists(RealmResource realmResource, String username, String rawPassword) {
    List<UserRepresentation> existingUsers = realmResource.users().searchByUsername(username, true);
    if (!existingUsers.isEmpty()) {
      return existingUsers.get(0).getId();
    }

    UserRepresentation user = new UserRepresentation();
    user.setUsername(username);
    user.setEmail(username + "@eschborn.local");
    user.setFirstName(username);
    user.setLastName("user");
    user.setEnabled(true);
    user.setCredentials(List.of(passwordCredential(rawPassword)));

    try (Response response = realmResource.users().create(user)) {
      if (response.getStatus() < 200 || response.getStatus() >= 300) {
        throw new IllegalStateException("Keycloak user creation failed for user " + username + " with status " + response.getStatus());
      }

      String location = response.getHeaderString("Location");
      if (location == null || !location.contains("/")) {
        throw new IllegalStateException("Keycloak did not return a user location for " + username);
      }
      return location.substring(location.lastIndexOf('/') + 1);
    }
  }

  private CredentialRepresentation passwordCredential(String rawPassword) {
    CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
    credentialRepresentation.setType(CredentialRepresentation.PASSWORD);
    credentialRepresentation.setTemporary(false);
    credentialRepresentation.setValue(rawPassword);
    return credentialRepresentation;
  }

  private void assignRealmRoles(RealmResource realmResource, String userId, Set<String> roleNames) {
    UserResource userResource = realmResource.users().get(userId);

    Set<String> existingRoles = userResource.roles().realmLevel().listAll().stream()
      .map(RoleRepresentation::getName)
      .filter(Objects::nonNull)
      .collect(Collectors.toSet());

    if (existingRoles.equals(roleNames)) {
      return;
    }

    List<RoleRepresentation> rolesToRemove = userResource.roles().realmLevel().listAll().stream()
      .filter(role -> role.getName() != null && !roleNames.contains(role.getName()))
      .toList();

    if (!rolesToRemove.isEmpty()) {
      userResource.roles().realmLevel().remove(rolesToRemove);
    }

    List<RoleRepresentation> rolesToAdd = roleNames.stream()
      .filter(roleName -> !existingRoles.contains(roleName))
      .map(roleName -> realmResource.roles().get(roleName).toRepresentation())
      .toList();

    if (!rolesToAdd.isEmpty()) {
      userResource.roles().realmLevel().add(rolesToAdd);
    }
  }

  public record KeycloakUserSnapshot(
    String referenceId,
    String username,
    String displayName,
    String email,
    boolean enabled,
    Set<String> roles
  ) {
  }
}

