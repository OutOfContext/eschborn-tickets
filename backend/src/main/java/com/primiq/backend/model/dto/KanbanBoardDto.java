package com.primiq.backend.model.dto;

import com.primiq.backend.model.dao.BoardLane;
import lombok.Data;

import java.util.List;

@Data
public class KanbanBoardDto {

    private BoardTypeDto type;
    private List<BoardLane> lanes;

}