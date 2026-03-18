package com.primiq.backend.model.dao;

import com.primiq.backend.model.dao.base.AbstractBoard;
import com.primiq.backend.model.dao.base.AbstractBoardContent;
import com.primiq.backend.model.dto.BoardType;
import jakarta.persistence.Entity;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@SuperBuilder
@NoArgsConstructor
public class Board extends AbstractBoard<BoardContent<?>>   {
 private BoardType type;

}
