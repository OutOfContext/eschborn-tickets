package com.primiq.backend.model.dao.base;

import com.primiq.backend.model.dao.BaseEntity;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.sql.Timestamp;


@MappedSuperclass
@EqualsAndHashCode(callSuper = true)
@Data
public abstract class AbstractBudget extends BaseEntity {

  private BigDecimal planBudget;
  private BigDecimal currentBudget;
  private String name;
  private Timestamp releaseDate;
  private Timestamp endDate;
  private boolean isActive;


}
