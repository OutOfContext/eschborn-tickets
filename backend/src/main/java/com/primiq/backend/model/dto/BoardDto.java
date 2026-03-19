package com.primiq.backend.model.dto;

import com.primiq.backend.model.dao.base.AbstractUser;
import lombok.Data;

import java.sql.Timestamp;
import java.util.UUID;

@Data
public class BoardDto {
  private UUID id;
  private Timestamp createdAt;
  private Timestamp updatedAt;
  private String lastUser;
  private AbstractUser owner;
  private BoardTypeDto type;
  private String name;
  private String description;
}
