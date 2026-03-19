package com.primiq.backend.model.dao.base;

import com.primiq.backend.model.dao.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@MappedSuperclass
public class AbstractUser extends BaseEntity {

  @Column(nullable = false)
  private String displayName;

  @Embedded
  private AbstractUserProfile userProfile;

  @Column(nullable = false, unique = true)
  private String referenceId; // dient zur Identifikation in Keycloak (Keycloak-User ID)


}
