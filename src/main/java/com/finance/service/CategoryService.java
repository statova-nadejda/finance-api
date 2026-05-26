package com.finance.service;

import com.finance.exception.InvalidDataException;
import com.finance.exception.ResourceNotFoundException;
import com.finance.model.Category;
import com.finance.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<Category> getAll() {
        return categoryRepository.findAll();
    }

    public Category getById(Long id) {
        validateId(id);
        return findExisting(id);
    }

    public Category create(Category category) {
        validateCategory(category);
        return categoryRepository.save(category);
    }

    public Category update(Long id, Category categoryDetails) {
        validateId(id);
        validateCategory(categoryDetails);

        Category category = findExisting(id);

        category.setName(categoryDetails.getName());
        category.setType(categoryDetails.getType());

        return categoryRepository.save(category);
    }

    public void delete(Long id) {
        validateId(id);
        categoryRepository.delete(findExisting(id));
    }

    private Category findExisting(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
    }

    private void validateId(Long id) {
        if (id == null || id <= 0) {
            throw new InvalidDataException("Id must be greater than 0");
        }
    }

    private void validateCategory(Category category) {
        if (category == null) {
            throw new InvalidDataException("Category data is required");
        }
    }
}
