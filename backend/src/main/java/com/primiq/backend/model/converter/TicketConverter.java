package com.primiq.backend.model.converter;

import com.primiq.backend.model.dao.Ticket;
import com.primiq.backend.model.dto.TicketDto;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class TicketConverter {
    public TicketDto toDto(Ticket entity) {
        TicketDto dto = new TicketDto();
        // dto.setId(entity.getId());
        // dto.setTitle(entity.getTitle());
        // dto.setStatus(entity.getStatus());
        // weitere Felder
        return dto;
    }

    public Ticket toEntity(TicketDto dto) {
        Ticket entity = new Ticket();
        // entity.setId(dto.getId());
        // entity.setTitle(dto.getTitle());
        // entity.setStatus(dto.getStatus());
        // weitere Felder
        return entity;
    }

    public List<TicketDto> toDtoList(List<Ticket> entities) {
        // Mapping für Listen
        return null;
    }
}

