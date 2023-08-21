package ru.practicum.category.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.category.dto.CategoryDtoMapper;
import ru.practicum.category.dto.InputCategoryDto;
import ru.practicum.category.dto.OutputCategoryDto;
import ru.practicum.category.model.Category;
import ru.practicum.category.repository.CategoryRepo;
import ru.practicum.events.repo.EventRepo;
import ru.practicum.exception.exceptions.CategoryDeleteException;
import ru.practicum.exception.exceptions.CategoryUniqueNameException;
import ru.practicum.exception.exceptions.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final EventRepo eventRepo;
    private final CategoryRepo categoryRepo;

    @Override
    @Transactional
    public OutputCategoryDto createCategory(InputCategoryDto inputCategoryDto) {
        if (categoryRepo.existsByName(inputCategoryDto.getName())) {
            throw new CategoryUniqueNameException("Name already exist");
        }
        return CategoryDtoMapper.toOutputCategoryDto(
                categoryRepo.save(CategoryDtoMapper.toCategory(inputCategoryDto)));
    }

    @Override
    @Transactional
    public void deleteCategory(Long catId) {
        if (!categoryRepo.existsById(catId)) {
            throw new NotFoundException("Category with id %d does not exist");
        }
        if (eventRepo.existsByCategory_Id(catId)) {
            throw new CategoryDeleteException("Category with id used in Events");
        }
        categoryRepo.deleteById(catId);
    }

    @Override
    @Transactional
    public OutputCategoryDto patchCategory(Long catId, InputCategoryDto inputCategoryDto) {
        if (!categoryRepo.existsById(catId)) {
            throw new NotFoundException("Category with id %d does not exist");
        }
        if (categoryRepo.existsByNameAndIdNot(inputCategoryDto.getName(), catId)) {
            throw new CategoryUniqueNameException("Name already exist");
        }
        return CategoryDtoMapper.toOutputCategoryDto(
                CategoryDtoMapper.updateCategory(inputCategoryDto, catId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<OutputCategoryDto> getAllCategories(Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from / size, size);
        return categoryRepo.findAll(pageable).stream()
                .map(CategoryDtoMapper::toOutputCategoryDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public OutputCategoryDto getCategoryById(Long catId) {
        Category category = categoryRepo.findById(catId)
                .orElseThrow(() -> new NotFoundException("Category with id %d does not exist"));
        return CategoryDtoMapper.toOutputCategoryDto(category);
    }
}
