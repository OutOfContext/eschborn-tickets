package com.primiq.backend.model.dto;

import com.primiq.backend.model.dao.base.AbstractUser;
import lombok.Data;

import java.sql.Timestamp;
import java.util.UUID;

@Data
public class TicketDto{
    private UUID id;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private String lastUser;
    private AbstractUser owner;
    private String title;
    private String description;
    private String status;

}
