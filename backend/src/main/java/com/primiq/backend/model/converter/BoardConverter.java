package com.primiq.backend.model.converter;

import com.primiq.backend.model.dao.Board;
import com.primiq.backend.model.dao.BoardTypeEnum;
import com.primiq.backend.model.dto.BoardDto;
import com.primiq.backend.model.dto.BoardTypeDto;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class BoardConverter implements EntityConverter<UUID, Board, BoardDto>{

    @Override
    public List<BoardDto> convertAll(List<Board> all) {
        return List.of();
    }

    @Override
    public Page<BoardDto> convertAll(Page<Board> all) {
        return null;
    }

    @Override
    public BoardDto convert(Board board) {
        return toDto(board);
    }

    public BoardDto toDto(Board board) {
        BoardDto dto = new BoardDto();
        dto.setName(board.getName());
        if (board.getType() != null) {
            dto.setType(BoardTypeDto.valueOf(board.getType().name()));
        }
        // weitere Felder
        return dto;
    }

    public Board toEntity(BoardDto dto) {
        Board board = new Board();
        board.setName(dto.getName());
        if (dto.getType() != null) {
            board.setType(BoardTypeEnum.valueOf(dto.getType().name()));
        }
        // weitere Felder
        return board;
    }
}
