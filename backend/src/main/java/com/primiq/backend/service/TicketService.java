package com.primiq.backend.service;

import com.primiq.backend.model.converter.EntityConverter;
import com.primiq.backend.model.converter.TicketConverter;
import com.primiq.backend.model.creater.EntityCreater;
import com.primiq.backend.model.creater.TicketCreator;
import com.primiq.backend.model.creater.TicketUpdater;
import com.primiq.backend.model.dao.Ticket;
import com.primiq.backend.model.dto.TicketDto;
import com.primiq.backend.model.repository.TicketRepository;
import com.primiq.backend.model.updater.EntityUpdater;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;
@Service
@RequiredArgsConstructor
public class TicketService implements EntityService<UUID, Ticket, TicketDto> {

    private final TicketConverter ticketConverter;
    private final TicketCreator ticketCreator;
    private final TicketUpdater ticketUpdater;
    private final TicketRepository ticketRepository;

    @Override
    public JpaRepository<Ticket, UUID> repository() {
        return ticketRepository;
    }

    @Override
    public TicketConverter converter() {
        return ticketConverter;
    }

    @Override
    public EntityCreater<Ticket, TicketDto> entityCreater() {
        return ticketCreator;
    }

    @Override
    public EntityUpdater<Ticket, TicketDto> entityUpdater() {
        return ticketUpdater;
    }


}