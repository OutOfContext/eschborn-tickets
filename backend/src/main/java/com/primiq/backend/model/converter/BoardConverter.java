package com.primiq.backend.model.converter;

import com.primiq.backend.model.dao.Board;
import com.primiq.backend.model.dto.BoardDto;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Component
public class BoardConverter implements EntityConverter<UUID, Board, BoardDto>{

    @Override
    public Collection<BoardDto> convertAll(Collection<Board> all) {
        return List.of();
    }

    @Override
    public Page<BoardDto> convertAll(Page<Board> all) {
        return null;
    }

    @Override
    public BoardDto convert(Board board) {
        return null;
    }
}
