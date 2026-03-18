package com.primiq.backend.model.dto;

import com.primiq.backend.model.dao.BoardLane;
import com.primiq.backend.model.dao.base.AbstractBoard;
import lombok.Data;

import java.util.List;

@Data
public class KanbanBoardDto {

    private BoardType type;
    private List<BoardLane> lanes;

}