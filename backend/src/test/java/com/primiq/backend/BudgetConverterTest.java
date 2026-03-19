package com.primiq.backend;

import static org.assertj.core.api.Assertions.assertThat;

import com.primiq.backend.model.converter.BudgetConverter;
import com.primiq.backend.model.dao.Budget;
import com.primiq.backend.model.dto.BudgetResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

class BudgetConverterTest {

    private BudgetConverter budgetConverter;

    @BeforeEach
    void setUp() {
        budgetConverter = new BudgetConverter();
    }

    @Test
    void toResponseDtoConvertsAllFields() {
        Budget budget = new Budget();
        budget.setId(UUID.randomUUID());
        budget.setName("Test Budget");
        budget.setPlanBudget(BigDecimal.valueOf(5000.50));
        budget.setDescription("Test Description");

        BudgetResponseDto result = budgetConverter.toResponseDto(budget);

        assertThat(result.getId()).isEqualTo(budget.getId());
        assertThat(result.getName()).isEqualTo("Test Budget");
        assertThat(result.getAmount()).isEqualTo(5000.50);
        assertThat(result.getDescription()).isEqualTo("Test Description");
    }

    @Test
    void toResponseDtoHandlesNullPlanBudget() {
        Budget budget = new Budget();
        budget.setId(UUID.randomUUID());
        budget.setName("No Amount Budget");
        budget.setPlanBudget(null);

        BudgetResponseDto result = budgetConverter.toResponseDto(budget);

        assertThat(result.getAmount()).isNull();
    }

    @Test
    void toResponseDtoHandlesNullId() {
        Budget budget = new Budget();
        budget.setId(null);
        budget.setName("New Budget");

        BudgetResponseDto result = budgetConverter.toResponseDto(budget);

        assertThat(result.getId()).isNull();
    }

    @Test
    void convertDelegatesToToResponseDto() {
        Budget budget = new Budget();
        budget.setId(UUID.randomUUID());
        budget.setName("Delegated Budget");
        budget.setPlanBudget(BigDecimal.valueOf(1000));

        BudgetResponseDto result = budgetConverter.convert(budget);

        assertThat(result.getName()).isEqualTo("Delegated Budget");
    }
}
