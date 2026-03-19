package com.primiq.backend.model.dto;

import java.util.Collection;
import com.primiq.backend.model.dao.Ticket;
import lombok.Data;

@Data
public class BoardContentDto {
    private Object content;
    private Collection<Ticket> tickets;

}

