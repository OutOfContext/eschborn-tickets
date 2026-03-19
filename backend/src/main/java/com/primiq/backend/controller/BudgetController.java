package com.primiq.backend.controller;

import com.primiq.backend.model.converter.BudgetConverter;
import com.primiq.backend.model.dao.Budget;
import com.primiq.backend.model.dto.BudgetRequestDto;
import com.primiq.backend.model.dto.BudgetResponseDto;

import com.primiq.backend.service.BudgetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/budgets")
public class BudgetController {
    private final BudgetService budgetService;
    private final BudgetConverter budgetConverter;

    @Autowired
    public BudgetController(BudgetService budgetService, BudgetConverter budgetConverter) {
        this.budgetService = budgetService;
        this.budgetConverter = budgetConverter;
    }

    @PostMapping
    public ResponseEntity<BudgetResponseDto> createBudget(@RequestBody BudgetRequestDto dto) {
        Budget budget = budgetService.createBudget(dto);
        return ResponseEntity.ok(budgetConverter.convert(budget));
    }

    @GetMapping
    public ResponseEntity<List<BudgetResponseDto>> getAllBudgets() {
        List<Budget> budgets = budgetService.getAllBudgets();
        List<BudgetResponseDto> dtos = budgets.stream().map(budgetConverter::convert).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BudgetResponseDto> getBudgetById(@PathVariable UUID id) {
        Optional<Budget> budgetOpt = budgetService.getBudgetById(id);
        return budgetOpt.map(budget -> ResponseEntity.ok(budgetConverter.convert(budget)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<BudgetResponseDto> updateBudget(@PathVariable UUID id, @RequestBody BudgetRequestDto dto) {
        Budget updated = budgetService.updateBudget(id, dto);
        return ResponseEntity.ok(budgetConverter.convert(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBudget(@PathVariable UUID id) {
        budgetService.deleteBudget(id);
        return ResponseEntity.noContent().build();
    }
}
