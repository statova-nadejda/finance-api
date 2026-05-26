package com.finance.service;

import com.finance.dto.BudgetRequest;
import com.finance.dto.BudgetResponse;
import com.finance.exception.InvalidDataException;
import com.finance.exception.ResourceNotFoundException;
import com.finance.exception.UserNotFoundException;
import com.finance.model.Budget;
import com.finance.model.Category;
import com.finance.model.User;
import com.finance.repository.BudgetRepository;
import com.finance.repository.CategoryRepository;
import com.finance.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BudgetService {

    private final BudgetRepository budgetRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    public List<BudgetResponse> getAll() {
        return budgetRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public BudgetResponse getById(Long id) {
        validateId(id);
        return toResponse(findExisting(id));
    }

    public BudgetResponse create(BudgetRequest request) {
        validateBudget(request);

        Budget budget = new Budget();
        budget.setLimitAmount(request.limitAmount());
        budget.setMonth(request.month());
        budget.setUser(findUser(request.userId()));
        budget.setCategory(findCategory(request.categoryId()));

        return toResponse(budgetRepository.save(budget));
    }

    public BudgetResponse update(Long id, BudgetRequest request) {
        validateId(id);
        validateBudget(request);

        Budget budget = findExisting(id);
        budget.setLimitAmount(request.limitAmount());
        budget.setMonth(request.month());
        budget.setUser(findUser(request.userId()));
        budget.setCategory(findCategory(request.categoryId()));

        return toResponse(budgetRepository.save(budget));
    }

    public void delete(Long id) {
        validateId(id);
        budgetRepository.delete(findExisting(id));
    }

    private Budget findExisting(Long id) {
        return budgetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Budget not found with id: " + id));
    }

    private User findUser(Long id) {
        validateRelatedId(id, "userId");
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
    }

    private Category findCategory(Long id) {
        validateRelatedId(id, "categoryId");
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
    }

    private BudgetResponse toResponse(Budget budget) {
        return new BudgetResponse(
                budget.getId(),
                budget.getLimitAmount(),
                budget.getMonth(),
                budget.getUser().getId(),
                budget.getCategory().getId()
        );
    }

    private void validateId(Long id) {
        if (id == null || id <= 0) {
            throw new InvalidDataException("Id must be greater than 0");
        }
    }

    private void validateRelatedId(Long id, String fieldName) {
        if (id == null || id <= 0) {
            throw new InvalidDataException(fieldName + " must be greater than 0");
        }
    }

    private void validateBudget(BudgetRequest budget) {
        if (budget == null) {
            throw new InvalidDataException("Budget data is required");
        }
    }
}
