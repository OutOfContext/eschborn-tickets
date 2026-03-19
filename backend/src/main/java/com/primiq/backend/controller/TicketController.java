package com.primiq.backend.controller;

import com.primiq.backend.model.dao.Ticket;
import com.primiq.backend.model.dto.TicketDto;
import com.primiq.backend.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
public class TicketController implements CrudController<UUID, Ticket, TicketDto>
{
  private  final TicketService service;

    @Override
  public TicketService service() {
    return service;
  }

}
