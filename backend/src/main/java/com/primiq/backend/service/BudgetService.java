package com.primiq.backend.service;

import com.primiq.backend.model.creater.BudgetCreater;
import com.primiq.backend.model.dao.Budget;
import com.primiq.backend.model.dto.BudgetRequestDto;
import com.primiq.backend.model.repository.BudgetRepository;
import com.primiq.backend.model.updater.BudgetUpdater;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BudgetService {
    private final BudgetRepository budgetRepository;
    private final BudgetCreater budgetCreater;
    private final BudgetUpdater budgetUpdater;

    public Budget createBudget(BudgetRequestDto dto) {
        Budget budget = budgetCreater.toEntity(dto);
        budgetCreater.createBudget(budget);
        return budgetRepository.save(budget);
    }

    public Budget updateBudget(UUID id, BudgetRequestDto dto) {
        Budget budget = budgetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(String.format("Budget with id [%s] not found", id)));
        budgetUpdater.updateBudget(budget, dto);
        return budgetRepository.save(budget);
    }

    public void deleteBudget(UUID id) {
        Budget budget = budgetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(String.format("Budget with id [%s] not found", id)));
        budgetRepository.delete(budget);
    }

    public List<Budget> getAllBudgets() {
        return budgetRepository.findAll();
    }

    public Optional<Budget> getBudgetById(UUID id) {
        return budgetRepository.findById(id);
    }
}
