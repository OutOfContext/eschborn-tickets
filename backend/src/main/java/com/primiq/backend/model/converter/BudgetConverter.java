package com.primiq.backend.model.converter;

import com.primiq.backend.model.dao.Budget;
import com.primiq.backend.model.dto.BudgetResponseDto;

public class BudgetConverter {
    public BudgetResponseDto toResponseDto(Budget budget) {
        BudgetResponseDto dto = new BudgetResponseDto();
        dto.setId(budget.getId());
        dto.setName(budget.getName());
        dto.setAmount(budget.getPlanBudget() != null ? budget.getPlanBudget().doubleValue() : null);
        dto.setDescription(budget.getDescription());
        return dto;
    }

    public BudgetResponseDto convert(Budget budget) {
        return toResponseDto(budget);
    }
}
