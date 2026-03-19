package com.primiq.backend.model.repository;

import com.primiq.backend.model.dao.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TicketRepository extends JpaRepository<Ticket, UUID> {
}
