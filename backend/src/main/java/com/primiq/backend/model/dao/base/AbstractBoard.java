package com.primiq.backend.model.dao.base;

import com.primiq.backend.model.dao.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class AbstractBoard<T> extends BaseEntity {

  private String type;
  private String name;
  private String description;
  private AbstractBoardContent<T> boardContent;
}
