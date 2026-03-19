package com.primiq.backend.model.converter;

import com.primiq.backend.model.dao.Board;
import com.primiq.backend.model.dto.BoardDto;
import java.util.Collection;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class BoardConverter implements EntityConverter<UUID, Board, BoardDto>{

    @Override
    public Collection<BoardDto> convertAllToDto(List<Board> all) {
        return List.of();
    }

    @Override
    public Collection<Board> convertAllToDao(List<BoardDto> all) {
        return List.of();
    }

    @Override
    public Page<BoardDto> convertAllToDto(Page<Board> all) {
        return null;
    }

    @Override
    public Page<Board> convertAllToDao(Page<BoardDto> all) {
        return null;
    }

    @Override
    public Board convertToDao(BoardDto dao) {
        return null;
    }

    @Override
    public BoardDto convertToDto(Board board) {
        return null;
    }
}
