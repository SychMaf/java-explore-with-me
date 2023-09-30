package ru.practicum.category.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.dtoPoint.category.InputCategoryDto;
import org.dtoPoint.category.OutputCategoryDto;
import ru.practicum.category.service.CategoryService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping("/admin/categories")
    @ResponseStatus(HttpStatus.CREATED) //201
    public OutputCategoryDto createCategory(@RequestBody @Valid InputCategoryDto inputCategoryDto) {
        log.trace("ADMIN CONTROLLER: request to create category: {}", inputCategoryDto);
        return categoryService.createCategory(inputCategoryDto);
    }

    @DeleteMapping("/admin/categories/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)  //204
    public void removeCategory(@PathVariable Long catId) {
        log.trace("ADMIN CONTROLLER: request to delete category with id: {}", catId);
        categoryService.deleteCategory(catId);
    }

    @PatchMapping("/admin/categories/{catId}")
    @ResponseStatus(HttpStatus.OK)  //200
    public OutputCategoryDto patchCategory(@PathVariable Long catId,
                                           @RequestBody @Valid InputCategoryDto inputCategoryDto) {
        log.trace("ADMIN CONTROLLER: request to update category: {}", inputCategoryDto);
        return categoryService.patchCategory(catId, inputCategoryDto);
    }

    @GetMapping("/categories")
    @ResponseStatus(HttpStatus.OK)  //200
    public List<OutputCategoryDto> getAllCategories(@RequestParam(required = false, defaultValue = "0") @PositiveOrZero Integer from,
                                                    @RequestParam(required = false, defaultValue = "10") @Positive Integer size) {
        log.trace("PUBLIC CONTROLLER: request to get all category");
        return categoryService.getAllCategories(from, size);
    }

    @GetMapping("/categories/{catId}")
    @ResponseStatus(HttpStatus.OK)  //200
    public OutputCategoryDto getCategoryById(@PathVariable Long catId) {
        log.trace("PUBLIC CONTROLLER: request to find category with id: {}", catId);
        return categoryService.getCategoryById(catId);
    }
}
