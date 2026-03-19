package com.primiq.backend.unit;

import com.primiq.backend.model.dao.Ticket;
import com.primiq.backend.model.dto.TicketDto;
import com.primiq.backend.service.EntityService;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import org.mockito.Mockito;
import com.primiq.backend.model.converter.TicketConverter;
import com.primiq.backend.model.creater.TicketCreator;
import com.primiq.backend.model.creater.TicketUpdater;
import com.primiq.backend.model.repository.TicketRepository;
import com.primiq.backend.service.TicketService;

public class TicketServiceTest {
    @Test
    void testService() {
        // Arrange
        TicketRepository ticketRepository = Mockito.mock(TicketRepository.class);
       // when(ticketRepository.save(Mockito.any(Ticket.class))).thenReturn(new Ticket());
        TicketConverter ticketConverter = new TicketConverter();
        TicketCreator ticketCreator = new TicketCreator();
        TicketUpdater ticketUpdater = new TicketUpdater();
        TicketService service = new TicketService(ticketConverter, ticketCreator, ticketUpdater, ticketRepository);

        TicketDto dto = new TicketDto();
        dto.setTitle("Test");
        dto.setDescription("Beschreibung");
        dto.setStatus("OPEN");
        dto.setPriority("HIGH");
        dto.setAssignee("Anna");

        // Act
        Ticket ticket = ticketCreator.create(dto);
        TicketDto converted = ticketConverter.convertToDto(ticket);

        // Assert
        assertEquals(dto.getTitle(), converted.getTitle());
        assertEquals(dto.getDescription(), converted.getDescription());
        assertEquals(dto.getStatus(), converted.getStatus());
        assertEquals(dto.getPriority(), converted.getPriority());
        assertEquals(dto.getAssignee(), converted.getAssignee());
    }

    @Test
    void testCreate() {
        TicketRepository ticketRepository = Mockito.mock(TicketRepository.class);
        Ticket sample = new Ticket();

        sample.setTitle("Test");
        sample.setDescription("Beschreibung");
        sample.setStatus("OPEN");
        sample.setPriority("HIGH");
        sample.setAssignee("Anna");

        when(ticketRepository.save(Mockito.any(Ticket.class))).thenReturn(sample);
        TicketConverter ticketConverter = new TicketConverter();
        TicketCreator ticketCreator = new TicketCreator();
        TicketUpdater ticketUpdater = new TicketUpdater();
        TicketService service = new TicketService(ticketConverter, ticketCreator, ticketUpdater, ticketRepository);

        TicketDto dto = new TicketDto();
        dto.setTitle("Test");
        dto.setDescription("Beschreibung");
        dto.setStatus("OPEN");
        dto.setPriority("HIGH");
        dto.setAssignee("Anna");

        TicketDto created = service.create(dto);
        assertNotNull(created);
        assertEquals(dto.getTitle(), created.getTitle());
        assertEquals(dto.getDescription(), created.getDescription());
        assertEquals(dto.getStatus(), created.getStatus());
        assertEquals(dto.getPriority(), created.getPriority());
        assertEquals(dto.getAssignee(), created.getAssignee());
    }

    @Test
    void testUpdate() {
        TicketRepository ticketRepository = Mockito.mock(TicketRepository.class);
        TicketConverter ticketConverter = new TicketConverter();
        TicketCreator ticketCreator = new TicketCreator();
        TicketUpdater ticketUpdater = new TicketUpdater();
        TicketService service = new TicketService(ticketConverter, ticketCreator, ticketUpdater, ticketRepository);

        UUID id = UUID.randomUUID();
        TicketDto dto = new TicketDto();
        dto.setTitle("Update");
        dto.setDescription("UpdateDesc");
        dto.setStatus("IN_PROGRESS");
        dto.setPriority("LOW");
        dto.setAssignee("Anna");

        Ticket ticket = ticketCreator.create(dto);
        when(ticketRepository.findById(id)).thenReturn(java.util.Optional.of(ticket));
        when(ticketRepository.save(Mockito.any(Ticket.class))).thenReturn(ticket);

        TicketDto updated = service.update(id, dto);
        assertNotNull(updated);
        assertEquals(dto.getTitle(), updated.getTitle());
        assertEquals(dto.getDescription(), updated.getDescription());
        assertEquals(dto.getStatus(), updated.getStatus());
        assertEquals(dto.getPriority(), updated.getPriority());
        assertEquals(dto.getAssignee(), updated.getAssignee());
    }

    @Test
    void testFetchOne() {
        TicketRepository ticketRepository = Mockito.mock(TicketRepository.class);
        TicketConverter ticketConverter = new TicketConverter();
        TicketCreator ticketCreator = new TicketCreator();
        TicketUpdater ticketUpdater = new TicketUpdater();
        TicketService service = new TicketService(ticketConverter, ticketCreator, ticketUpdater, ticketRepository);

        UUID id = UUID.randomUUID();
        TicketDto dto = new TicketDto();
        dto.setTitle("Fetch");
        dto.setDescription("FetchDesc");
        dto.setStatus("CLOSED");
        dto.setPriority("MEDIUM");
        dto.setAssignee("Anna");

        Ticket ticket = ticketCreator.create(dto);
        when(ticketRepository.findById(id)).thenReturn(java.util.Optional.of(ticket));

        TicketDto fetched = service.fetchOne(id);
        assertNotNull(fetched);
        assertEquals(dto.getTitle(), fetched.getTitle());
        assertEquals(dto.getDescription(), fetched.getDescription());
        assertEquals(dto.getStatus(), fetched.getStatus());
        assertEquals(dto.getPriority(), fetched.getPriority());
        assertEquals(dto.getAssignee(), fetched.getAssignee());
    }

    @Test
    void testDelete() {
        TicketRepository ticketRepository = Mockito.mock(TicketRepository.class);
        TicketConverter ticketConverter = new TicketConverter();
        TicketCreator ticketCreator = new TicketCreator();
        TicketUpdater ticketUpdater = new TicketUpdater();
        TicketService service = new TicketService(ticketConverter, ticketCreator, ticketUpdater, ticketRepository);

        UUID id = UUID.randomUUID();
        Mockito.doNothing().when(ticketRepository).deleteById(id);
        String result = service.delete(id);
        assertTrue(result.contains("deleted successfully"));
    }
}
