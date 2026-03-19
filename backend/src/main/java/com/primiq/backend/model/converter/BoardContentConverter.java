package com.primiq.backend.model.converter;

import com.primiq.backend.model.dao.BoardContent;
import com.primiq.backend.model.dto.BoardContentDto;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class BoardContentConverter {
    public BoardContentDto toDto(BoardContent<Object> entity) {
        BoardContentDto dto = new BoardContentDto();
        dto.setContent(entity.getContent());
        dto.setTickets(entity.getTickets());
        // weitere Felder
        return dto;
    }

    public BoardContent<Object> toEntity(BoardContentDto dto) {
        BoardContent<Object> entity = new BoardContent<>();
        entity.setContent(dto.getContent());
        entity.setTickets(dto.getTickets());
        // weitere Felder
        return entity;
    }

    public List<BoardContentDto> toDtoList(List<BoardContent<Object>> entities) {
        return entities.stream()
                .map(this::toDto)
                .toList();
    }
}
