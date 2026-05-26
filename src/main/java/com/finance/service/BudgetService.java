package com.finance.service;

import com.finance.exception.InvalidDataException;
import com.finance.exception.ResourceNotFoundException;
import com.finance.model.Budget;
import com.finance.repository.BudgetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BudgetService {

    private final BudgetRepository budgetRepository;

    public List<Budget> getAll() {
        return budgetRepository.findAll();
    }

    public Budget getById(Long id) {
        validateId(id);
        return findExisting(id);
    }

    public Budget create(Budget budget) {
        validateBudget(budget);
        return budgetRepository.save(budget);
    }

    public Budget update(Long id, Budget budgetDetails) {
        validateId(id);
        validateBudget(budgetDetails);

        Budget budget = findExisting(id);

        budget.setLimitAmount(budgetDetails.getLimitAmount());
        budget.setMonth(budgetDetails.getMonth());
        budget.setUser(budgetDetails.getUser());
        budget.setCategory(budgetDetails.getCategory());

        return budgetRepository.save(budget);
    }

    public void delete(Long id) {
        validateId(id);
        budgetRepository.delete(findExisting(id));
    }

    private Budget findExisting(Long id) {
        return budgetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Budget not found with id: " + id));
    }

    private void validateId(Long id) {
        if (id == null || id <= 0) {
            throw new InvalidDataException("Id must be greater than 0");
        }
    }

    private void validateBudget(Budget budget) {
        if (budget == null) {
            throw new InvalidDataException("Budget data is required");
        }
    }
}
