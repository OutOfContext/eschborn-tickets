package com.primiq.backend.model.dao.base;

import com.primiq.backend.model.dao.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class AbstractUser extends BaseEntity {

  private String displayName;
  private AbstractUserProfile userProfile;
  private String referenceId; // dient zur Identifikation in Keycloak (Keycloak-User ID)


}
