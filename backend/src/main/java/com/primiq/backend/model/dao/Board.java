package com.primiq.backend.model.dao;

import com.primiq.backend.model.dao.base.AbstractBoard;
import com.primiq.backend.model.dto.BoardTypeDto;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@SuperBuilder
@NoArgsConstructor
public class Board extends AbstractBoard<BoardContent<?>>   {
  @Enumerated(EnumType.STRING)
  private BoardTypeEnum type;

    public BoardTypeEnum getType() {
        return type;
    }

    public void setType(BoardTypeEnum type) {
        this.type = type;
    }
}
