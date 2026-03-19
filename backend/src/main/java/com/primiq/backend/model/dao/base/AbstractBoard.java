package com.primiq.backend.model.dao.base;

import com.primiq.backend.model.dao.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
public class AbstractBoard<T extends AbstractBoardContent> extends BaseEntity {
  private String name;
  private String description;
  private T boardContent;


}
