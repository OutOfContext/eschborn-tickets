package com.primiq.backend.model.dao.base;

import com.primiq.backend.model.dao.BaseEntity;
import java.util.Collection;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.catalina.users.AbstractUser;

@EqualsAndHashCode(callSuper = true)
@Data
public abstract class AbstractTeam extends BaseEntity {

  private String name;
  private String description;
  private Collection<AbstractUser> members;
  private String referenceId;
}
