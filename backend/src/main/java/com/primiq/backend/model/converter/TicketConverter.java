package com.primiq.backend.model.converter;

import com.primiq.backend.model.dao.Ticket;
import com.primiq.backend.model.dto.TicketDto;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Component
public class TicketConverter implements EntityConverter<UUID, Ticket, TicketDto> {

    @Override
    public Collection<TicketDto> convertAllToDto(Collection<Ticket> all) {
        return all.stream().map(this::convertToDto).toList();
    }

    @Override
    public Collection<Ticket> convertAllToDao(Collection<TicketDto> all) {
        return all.stream().map(this::convertToDao).toList();
    }

    @Override
    public Page<TicketDto> convertAllToDto(Page<Ticket> all) {
        return all.map(this::convertToDto);
    }

    @Override
    public Page<Ticket> convertAllToDao(Page<TicketDto> all) {
        return all.map(this::convertToDao);
    }

    @Override
    public TicketDto convertToDto(Ticket ticket) {
        TicketDto  ticketDto = new TicketDto();
        ticketDto.setTitle(ticket.getTitle());
        ticketDto.setPriority(ticket.getPriority());
        ticketDto.setStatus(ticket.getStatus());
        ticketDto.setDescription(ticket.getDescription());
        ticketDto.setAssignee(ticket.getAssignee());
        return ticketDto;
    }

    @Override
    public Ticket convertToDao(TicketDto ticketDto) {
      Ticket ticket = new Ticket();
      ticket.setStatus(ticketDto.getStatus());
      ticket.setAssignee(ticketDto.getAssignee());
      ticket.setPriority(ticketDto.getPriority());
      ticket.setTitle(ticketDto.getTitle());
      ticket.setDescription(ticketDto.getDescription());
      return ticket;

    }
}
