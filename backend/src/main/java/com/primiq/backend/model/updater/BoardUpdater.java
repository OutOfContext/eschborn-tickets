package com.primiq.backend.model.updater;

import com.primiq.backend.model.dao.Board;
import com.primiq.backend.model.dto.BoardDto;
import org.springframework.stereotype.Component;

@Component
public class BoardUpdater implements EntityUpdater<Board, BoardDto> {

    @Override
    public Board update(Board result, BoardDto request) {
        return null;
    }
}
