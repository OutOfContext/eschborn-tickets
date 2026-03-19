package com.primiq.backend.model.dto;

import lombok.Data;

import java.util.UUID;

@Data

public class BudgetResponseDto {
    private UUID id;
    private String name;
    private Double amount;
    private String description;

}

