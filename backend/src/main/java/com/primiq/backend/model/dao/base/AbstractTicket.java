package com.primiq.backend.model.dao.base;

import com.primiq.backend.model.dao.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class AbstractTicket extends BaseEntity {

  private String slug;
  private String title;
  private String description;
  private String status;
  private String priority;
  private AbstractUser assignee;
  private String category;

}
