package com.primiq.backend.model.creater;

import com.primiq.backend.model.dao.Ticket;
import com.primiq.backend.model.dto.TicketDto;
import com.primiq.backend.model.updater.EntityUpdater;
import org.springframework.stereotype.Component;

@Component
public class TicketUpdater implements EntityUpdater<Ticket, TicketDto> {
  @Override
  public Ticket update(Ticket entity, TicketDto request) {
    entity.setStatus(request.getStatus());
    entity.setDescription(request.getDescription());
    entity.setTitle(request.getTitle());
    entity.setPriority(request.getPriority());
    entity.setAssignee(request.getAssignee());
      return entity;
  }
}
