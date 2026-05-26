package com.finance.service;

import com.finance.dto.CategoryRequest;
import com.finance.dto.CategoryResponse;
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

    public List<CategoryResponse> getAll() {
        return categoryRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public CategoryResponse getById(Long id) {
        validateId(id);
        return toResponse(findExisting(id));
    }

    public CategoryResponse create(CategoryRequest request) {
        validateCategory(request);

        Category category = new Category();
        category.setName(request.name());
        category.setType(request.type());

        return toResponse(categoryRepository.save(category));
    }

    public CategoryResponse update(Long id, CategoryRequest request) {
        validateId(id);
        validateCategory(request);

        Category category = findExisting(id);
        category.setName(request.name());
        category.setType(request.type());

        return toResponse(categoryRepository.save(category));
    }

    public void delete(Long id) {
        validateId(id);
        categoryRepository.delete(findExisting(id));
    }

    private Category findExisting(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
    }

    private CategoryResponse toResponse(Category category) {
        return new CategoryResponse(category.getId(), category.getName(), category.getType());
    }

    private void validateId(Long id) {
        if (id == null || id <= 0) {
            throw new InvalidDataException("Id must be greater than 0");
        }
    }

    private void validateCategory(CategoryRequest category) {
        if (category == null) {
            throw new InvalidDataException("Category data is required");
        }
    }
}
