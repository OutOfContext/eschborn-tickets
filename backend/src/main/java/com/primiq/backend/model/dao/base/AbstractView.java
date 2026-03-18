package com.primiq.backend.model.dao.base;

import com.primiq.backend.model.dao.BaseEntity;
import java.util.Collection;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@SuperBuilder
public class AbstractView<T extends AbstractBoardContent> extends BaseEntity {

  private AbstractBoard<T> board;
  private Collection<AbstractFilter> filters;

}
