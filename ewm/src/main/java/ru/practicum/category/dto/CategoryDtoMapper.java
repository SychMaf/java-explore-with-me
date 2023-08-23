package ru.practicum.category.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.category.model.Category;

@UtilityClass
public class CategoryDtoMapper {
    public Category toCategory(InputCategoryDto inputCategoryDto) {
        return Category.builder()
                .name(inputCategoryDto.getName())
                .build();
    }

    public OutputCategoryDto toOutputCategoryDto(Category category) {
        return OutputCategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }

    public Category updateCategory(InputCategoryDto inputCategoryDto, Long catId) {
        return Category.builder()
                .id(catId)
                .name(inputCategoryDto.getName())
                .build();
    }
}
