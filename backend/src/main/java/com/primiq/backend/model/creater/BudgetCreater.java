package com.primiq.backend.model.creater;

import com.primiq.backend.model.dao.Budget;
import com.primiq.backend.model.dto.BudgetRequestDto;

import java.math.BigDecimal;

public class BudgetCreater {
    public Budget toEntity(BudgetRequestDto dto) {
        Budget budget = new Budget();
        budget.setName(dto.getName());
        if (dto.getAmount() != null) {
            budget.setPlanBudget(BigDecimal.valueOf(dto.getAmount()));
        }
        budget.setCurrentBudget(BigDecimal.ZERO);
        budget.setActive(true);
        budget.setDescription(dto.getDescription());
        return budget;
    }

    public void createBudget(Budget budget) {
        if (budget.getCurrentBudget() == null) {
            budget.setCurrentBudget(BigDecimal.ZERO);
        }
    }
}
