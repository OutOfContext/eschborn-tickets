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
    public Collection<BoardDto> convertAllToDto(Collection<Board> all) {
        return all.stream().map(this::convertToDto).toList();
    }

    @Override
    public Collection<Board> convertAllToDao(Collection<BoardDto> all) {
        return all.stream().map(this::convertToDao).toList();
    }

    @Override
    public Page<BoardDto> convertAllToDto(Page<Board> all) {
        return all.map(this::convertToDto);
    }

    @Override
    public Page<Board> convertAllToDao(Page<BoardDto> all) {

       return all.map(this::convertToDao);
    }

    @Override
    public BoardDto convertToDto(Board board) {
        BoardDto boardDto= new BoardDto();
        boardDto.setName(board.getName());
        return boardDto;
    }

    @Override
    public Board convertToDao(BoardDto dao) {
        return null;
    }
}
