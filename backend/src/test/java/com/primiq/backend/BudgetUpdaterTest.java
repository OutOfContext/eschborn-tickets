package com.primiq.backend;

import static org.assertj.core.api.Assertions.assertThat;

import com.primiq.backend.model.dao.Budget;
import com.primiq.backend.model.dto.BudgetRequestDto;
import com.primiq.backend.model.updater.BudgetUpdater;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

class BudgetUpdaterTest {

    private BudgetUpdater budgetUpdater;

    @BeforeEach
    void setUp() {
        budgetUpdater = new BudgetUpdater();
    }

    @Test
    void updateBudgetUpdatesAllProvidedFields() {
        Budget budget = new Budget();
        budget.setName("Original Name");
        budget.setPlanBudget(BigDecimal.valueOf(1000));
        budget.setDescription("Original Description");

        BudgetRequestDto dto = new BudgetRequestDto();
        dto.setName("Updated Name");
        dto.setAmount(2000.0);
        dto.setDescription("Updated Description");

        budgetUpdater.updateBudget(budget, dto);

        assertThat(budget.getName()).isEqualTo("Updated Name");
        assertThat(budget.getPlanBudget()).isEqualByComparingTo(BigDecimal.valueOf(2000.0));
        assertThat(budget.getDescription()).isEqualTo("Updated Description");
    }

    @Test
    void updateBudgetOnlyUpdatesNonNullFields() {
        Budget budget = new Budget();
        budget.setName("Original Name");
        budget.setPlanBudget(BigDecimal.valueOf(1000));
        budget.setDescription("Original Description");

        BudgetRequestDto dto = new BudgetRequestDto();
        dto.setName("Updated Name");
        dto.setAmount(null);  // sollte nicht aktualisieren
        dto.setDescription(null);  // sollte nicht aktualisieren

        budgetUpdater.updateBudget(budget, dto);

        assertThat(budget.getName()).isEqualTo("Updated Name");
        assertThat(budget.getPlanBudget()).isEqualByComparingTo(BigDecimal.valueOf(1000));
        assertThat(budget.getDescription()).isEqualTo("Original Description");
    }

    @Test
    void updateBudgetHandlesAllNullFields() {
        Budget budget = new Budget();
        budget.setName("Original Name");
        budget.setPlanBudget(BigDecimal.valueOf(1000));

        BudgetRequestDto dto = new BudgetRequestDto();
        // alle Felder sind null

        budgetUpdater.updateBudget(budget, dto);

        // Nichts sollte sich geändert haben
        assertThat(budget.getName()).isEqualTo("Original Name");
        assertThat(budget.getPlanBudget()).isEqualByComparingTo(BigDecimal.valueOf(1000));
    }

    @Test
    void updateBudgetUpdatesOnlyName() {
        Budget budget = new Budget();
        budget.setName("Old Name");
        budget.setPlanBudget(BigDecimal.valueOf(500));
        budget.setDescription("Old Description");

        BudgetRequestDto dto = new BudgetRequestDto();
        dto.setName("New Name");

        budgetUpdater.updateBudget(budget, dto);

        assertThat(budget.getName()).isEqualTo("New Name");
        assertThat(budget.getPlanBudget()).isEqualByComparingTo(BigDecimal.valueOf(500));
        assertThat(budget.getDescription()).isEqualTo("Old Description");
    }
}
