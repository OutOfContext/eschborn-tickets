package com.primiq.backend.model.dao.base;

import com.primiq.backend.model.dao.BaseEntity;
import java.util.Collection;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class AbstractView<T> extends BaseEntity {

  private AbstractBoard<T> board;
  private Collection<AbstractFilter> filters;

}
