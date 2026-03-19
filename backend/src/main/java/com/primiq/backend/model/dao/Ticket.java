package com.primiq.backend.model.dao;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;

@Entity
@AllArgsConstructor
public class Ticket extends BaseEntity{

    private String title;
    private String description;
    private String status;
    private int priority;
    private String assignee;

    public Ticket() {
    }


}
