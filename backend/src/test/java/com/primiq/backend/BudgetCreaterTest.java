package com.primiq.backend;

import static org.assertj.core.api.Assertions.assertThat;

import com.primiq.backend.model.creater.BudgetCreater;
import com.primiq.backend.model.dao.Budget;
import com.primiq.backend.model.dto.BudgetRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

class BudgetCreaterTest {

    private BudgetCreater budgetCreater;

    @BeforeEach
    void setUp() {
        budgetCreater = new BudgetCreater();
    }

    @Test
    void toEntityCreatesNewBudgetWithAllFields() {
        BudgetRequestDto dto = new BudgetRequestDto();
        dto.setName("New Budget");
        dto.setAmount(2500.0);
        dto.setDescription("Test Description");

        Budget result = budgetCreater.toEntity(dto);

        assertThat(result.getName()).isEqualTo("New Budget");
        assertThat(result.getPlanBudget()).isEqualByComparingTo(BigDecimal.valueOf(2500.0));
        assertThat(result.getCurrentBudget()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(result.isActive()).isTrue();
        assertThat(result.getDescription()).isEqualTo("Test Description");
    }

    @Test
    void toEntityHandlesNullAmount() {
        BudgetRequestDto dto = new BudgetRequestDto();
        dto.setName("No Amount Budget");
        dto.setAmount(null);

        Budget result = budgetCreater.toEntity(dto);

        assertThat(result.getPlanBudget()).isNull();
        assertThat(result.getCurrentBudget()).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    void createBudgetSetsDefaultCurrentBudgetIfNull() {
        Budget budget = new Budget();
        budget.setCurrentBudget(null);

        budgetCreater.createBudget(budget);

        assertThat(budget.getCurrentBudget()).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    void createBudgetKeepsExistingCurrentBudget() {
        Budget budget = new Budget();
        budget.setCurrentBudget(BigDecimal.valueOf(500));

        budgetCreater.createBudget(budget);

        assertThat(budget.getCurrentBudget()).isEqualByComparingTo(BigDecimal.valueOf(500));
    }
}
