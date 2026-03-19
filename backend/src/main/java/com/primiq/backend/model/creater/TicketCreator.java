package com.primiq.backend.model.creater;

import com.primiq.backend.model.dao.Ticket;
import com.primiq.backend.model.dto.TicketDto;
import org.springframework.stereotype.Component;

@Component
public class TicketCreator implements EntityCreater <Ticket, TicketDto> {
    @Override
    public Ticket create(TicketDto request) {
        Ticket ticket = new Ticket();
        ticket.setStatus(request.getStatus());
        ticket.setDescription(request.getDescription());
        ticket.setTitle(request.getTitle());
        ticket.setPriority(request.getPriority());
        ticket.setAssignee(request.getAssignee());
        return ticket;
    }
}
