package com.primiq.backend.model.dao;

import com.primiq.backend.model.dao.base.AbstractUser;
import java.sql.Timestamp;
import java.util.UUID;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@MappedSuperclass
@Data
@SuperBuilder
@NoArgsConstructor
public abstract class BaseEntity {
  @Id
  @GeneratedValue
  private UUID id;
  private Timestamp createdAt;
  private Timestamp updatedAt;
  private String lastUser;
  private AbstractUser owner;

}
