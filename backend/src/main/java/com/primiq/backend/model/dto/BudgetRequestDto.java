package com.primiq.backend.model.dto;

import lombok.Data;

@Data

public class BudgetRequestDto {
    private String name;
    private Double amount;
    private String description;
}


