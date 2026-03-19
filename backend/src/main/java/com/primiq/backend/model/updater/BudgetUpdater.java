package com.primiq.backend.model.updater;

import com.primiq.backend.model.dao.Budget;
import com.primiq.backend.model.dto.BudgetRequestDto;

import java.math.BigDecimal;

public class BudgetUpdater {
    public void updateBudget(Budget budget, BudgetRequestDto dto) {
        if (dto.getName() != null) {
            budget.setName(dto.getName());
        }
        if (dto.getAmount() != null) {
            budget.setPlanBudget(BigDecimal.valueOf(dto.getAmount()));
        }
         if (dto.getDescription() != null) {
            budget.setDescription(dto.getDescription());
        }
    }
}
