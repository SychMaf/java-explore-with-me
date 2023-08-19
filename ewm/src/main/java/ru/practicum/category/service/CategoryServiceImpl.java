package ru.practicum.category.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.category.dto.CategoryDtoMapper;
import ru.practicum.category.dto.InputCategoryDto;
import ru.practicum.category.dto.OutputCategoryDto;
import ru.practicum.category.model.Category;
import ru.practicum.category.repository.CategoryRepo;
import ru.practicum.exception.exceptions.CategoryUniqueNameException;
import ru.practicum.exception.exceptions.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepo categoryRepo;

    @Override
    public OutputCategoryDto createCategory(InputCategoryDto inputCategoryDto) {
        if (categoryRepo.existsByName(inputCategoryDto.getName())) {
            throw new CategoryUniqueNameException("Name already exist");
        }
        return CategoryDtoMapper.toOutputCategoryDto(
                categoryRepo.save(CategoryDtoMapper.toCategory(inputCategoryDto)));
    }

    @Override
    public void deleteCategory(Long catId) { //ПРОПИСАТЬ ЧЕК НА ПРОВЕРКУ НАЛИЧИЯ ТАКОЙ ТЕМЫ В ЕВЕНТАХ
        if (!categoryRepo.existsById(catId)) {
            throw new NotFoundException("Category with id %d does not exist");
        }
        categoryRepo.deleteById(catId);
    }

    @Override
    public OutputCategoryDto patchCategory(Long catId, InputCategoryDto inputCategoryDto) {
        if (categoryRepo.existsByName(inputCategoryDto.getName())) {
            throw new CategoryUniqueNameException("Name already exist");
        }
        if (!categoryRepo.existsById(catId)) {
            throw new NotFoundException("Category with id %d does not exist");
        }
        return CategoryDtoMapper.toOutputCategoryDto(
                CategoryDtoMapper.updateCategory(inputCategoryDto, catId));
    }

    @Override
    public List<OutputCategoryDto> getAllCategories(Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from / size, size);
        return categoryRepo.findAll(pageable).stream()
                .map(CategoryDtoMapper::toOutputCategoryDto)
                .collect(Collectors.toList());
    }

    @Override
    public OutputCategoryDto getCategoryById(Long catId) {
        Category category = categoryRepo.findById(catId)
                .orElseThrow(() -> new NotFoundException("Category with id %d does not exist"));
        return CategoryDtoMapper.toOutputCategoryDto(category);
    }
}
