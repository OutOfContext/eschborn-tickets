package com.primiq.backend.model.dao;

import com.primiq.backend.model.dao.base.AbstractUser;
import java.sql.Timestamp;
import java.util.UUID;
import lombok.Data;

@Data
public abstract class BaseEntity {

  private UUID id;
  private Timestamp createdAt;
  private Timestamp updatedAt;
  private String lastUser;
  private AbstractUser owner;

}
