package com.primiq.backend.model.dto;
import lombok.Data;
@Data
public class TicketDto {
    private String status;
    private String description;
    private String title;
    private String priority;
    private String assignee;
}
