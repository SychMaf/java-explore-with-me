package ru.practicum.category.service;

import ru.practicum.category.dto.InputCategoryDto;
import ru.practicum.category.dto.OutputCategoryDto;

import java.util.List;

public interface CategoryService {
    OutputCategoryDto createCategory(InputCategoryDto inputCategoryDto);
    void deleteCategory(Long catId);
    OutputCategoryDto patchCategory(Long catId, InputCategoryDto inputCategoryDto);
    List<OutputCategoryDto> getAllCategories(Integer from, Integer size);
    OutputCategoryDto getCategoryById(Long catId);
}
