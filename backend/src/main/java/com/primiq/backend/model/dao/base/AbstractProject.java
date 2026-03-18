package com.primiq.backend.model.dao.base;

import com.primiq.backend.model.dao.BaseEntity;
import java.sql.Timestamp;
import java.util.Collection;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public abstract class AbstractProject extends BaseEntity {

  private Timestamp startDate;
  private Timestamp endDate;
  private String name;
  private String description;
  private Collection<AbstractTeam> teams;
  private Collection<AbstractBudget> budgets;
  private Collection<AbstractBoard> boards;
}
