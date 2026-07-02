package com.financetracker.backend.service;

import com.financetracker.backend.dto.request.CategoryRequest;
import com.financetracker.backend.dto.response.CategoryResponse;
import com.financetracker.backend.entity.Category;
import com.financetracker.backend.entity.User;
import com.financetracker.backend.exception.ResourceNotFoundException;
import com.financetracker.backend.mapper.CategoryMapper;
import com.financetracker.backend.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private User getCurrentUser(){
        return (User) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
    }

    public CategoryResponse create(CategoryRequest request){
        Category category = Category.builder()
                .name(request.getName())
                .color(request.getColor())
                .icon(request.getIcon())
                .type(request.getType())
                .user(getCurrentUser())
                .build();

        return categoryMapper.toResponse(categoryRepository.save(category));
    }

    public List<CategoryResponse> getAll(){
        return categoryMapper.toResponseList(categoryRepository.findByUser(getCurrentUser()));
    }

    public CategoryResponse update(Long id, CategoryRequest categoryRequest){
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        if(!category.getUser().getId().equals(getCurrentUser().getId())){
          throw new AccessDeniedException("unauthorized");
        }
        category.setName(categoryRequest.getName());
        category.setColor(categoryRequest.getColor());
        category.setIcon(categoryRequest.getIcon());
        category.setType(categoryRequest.getType());
        return categoryMapper.toResponse(categoryRepository.save(category));
    }
    public String delete(Long id){
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        if(!category.getUser().getId().equals(getCurrentUser().getId())){
            throw new AccessDeniedException("unauthorized");
        }
        categoryRepository.delete(category);
        return "Deleted Successfully";
    }

    public void seedDefaultCategories(User user){
        List<String[]> defaults = List.of(
                new String[]{"Food", "EXPENSE", "#FF6B6B", "🍔"},
                new String[]{"Transport", "EXPENSE", "#4ECDC4", "🚗"},
                new String[]{"Shopping", "EXPENSE", "#45B7D1", "🛍️"},
                new String[]{"Bills", "EXPENSE", "#96CEB4", "�bill"},
                new String[]{"Salary", "INCOME", "#88D8B0", "💰"},
                new String[]{"Freelance", "INCOME", "#FFCC5C", "💻"}
        );
        defaults.forEach(d -> {
            Category category = Category.builder()
                    .name(d[0])
                    .type(d[1])
                    .color(d[2])
                    .icon(d[3])
                    .user(user)
                    .build();
            categoryRepository.save(category);
        });
    }

}
