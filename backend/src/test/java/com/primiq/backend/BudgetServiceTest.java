package com.primiq.backend;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.primiq.backend.model.creater.BudgetCreater;
import com.primiq.backend.model.dao.Budget;
import com.primiq.backend.model.dto.BudgetRequestDto;
import com.primiq.backend.model.repository.BudgetRepository;
import com.primiq.backend.model.updater.BudgetUpdater;
import com.primiq.backend.service.BudgetService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class BudgetServiceTest {

    @Mock
    private BudgetRepository budgetRepository;

    @Mock
    private BudgetCreater budgetCreater;

    @Mock
    private BudgetUpdater budgetUpdater;

    private BudgetService budgetService;

    @BeforeEach
    void setUp() {
        budgetService = new BudgetService(budgetRepository, budgetCreater, budgetUpdater);
    }

    @Test
    void createBudgetSavesAndReturnsBudget() {
        BudgetRequestDto dto = new BudgetRequestDto();
        dto.setName("Test Budget");
        dto.setAmount(1000.0);

        Budget budget = new Budget();
        budget.setName("Test Budget");
        budget.setPlanBudget(BigDecimal.valueOf(1000.0));

        when(budgetCreater.toEntity(dto)).thenReturn(budget);
        when(budgetRepository.save(budget)).thenReturn(budget);

        Budget result = budgetService.createBudget(dto);

        assertThat(result.getName()).isEqualTo("Test Budget");
        verify(budgetCreater).toEntity(dto);
        verify(budgetCreater).createBudget(budget);
        verify(budgetRepository).save(budget);
    }

    @Test
    void updateBudgetUpdatesExistingBudget() {
        UUID id = UUID.randomUUID();
        BudgetRequestDto dto = new BudgetRequestDto();
        dto.setName("Updated Budget");

        Budget existingBudget = new Budget();
        existingBudget.setId(id);
        existingBudget.setName("Original Budget");

        when(budgetRepository.findById(id)).thenReturn(Optional.of(existingBudget));
        when(budgetRepository.save(existingBudget)).thenReturn(existingBudget);

        Budget result = budgetService.updateBudget(id, dto);

        verify(budgetUpdater).updateBudget(existingBudget, dto);
        verify(budgetRepository).save(existingBudget);
    }

    @Test
    void updateBudgetThrowsExceptionWhenNotFound() {
        UUID id = UUID.randomUUID();
        BudgetRequestDto dto = new BudgetRequestDto();

        when(budgetRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> budgetService.updateBudget(id, dto))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("not found");
    }

    @Test
    void deleteBudgetDeletesExistingBudget() {
        UUID id = UUID.randomUUID();
        Budget budget = new Budget();
        budget.setId(id);

        when(budgetRepository.findById(id)).thenReturn(Optional.of(budget));

        budgetService.deleteBudget(id);

        verify(budgetRepository).delete(budget);
    }

    @Test
    void deleteBudgetThrowsExceptionWhenNotFound() {
        UUID id = UUID.randomUUID();

        when(budgetRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> budgetService.deleteBudget(id))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("not found");
    }

    @Test
    void getAllBudgetsReturnsAllBudgets() {
        Budget budget1 = new Budget();
        budget1.setName("Budget 1");
        Budget budget2 = new Budget();
        budget2.setName("Budget 2");

        when(budgetRepository.findAll()).thenReturn(List.of(budget1, budget2));

        List<Budget> result = budgetService.getAllBudgets();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getName()).isEqualTo("Budget 1");
    }

    @Test
    void getBudgetByIdReturnsBudgetWhenFound() {
        UUID id = UUID.randomUUID();
        Budget budget = new Budget();
        budget.setId(id);
        budget.setName("Found Budget");

        when(budgetRepository.findById(id)).thenReturn(Optional.of(budget));

        Optional<Budget> result = budgetService.getBudgetById(id);

        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("Found Budget");
    }

    @Test
    void getBudgetByIdReturnsEmptyWhenNotFound() {
        UUID id = UUID.randomUUID();

        when(budgetRepository.findById(id)).thenReturn(Optional.empty());

        Optional<Budget> result = budgetService.getBudgetById(id);

        assertThat(result).isEmpty();
    }
}
