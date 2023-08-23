package ru.practicum.validator;

import lombok.experimental.UtilityClass;
import ru.practicum.category.repository.CategoryRepo;
import ru.practicum.exception.exceptions.CategoryUniqueNameException;
import ru.practicum.exception.exceptions.NotFoundException;

@UtilityClass
public class CategoryValidator {
    public void checkCategoryExist(CategoryRepo categoryRepo, Long catId) {
        if (!categoryRepo.existsById(catId)) {
            throw new NotFoundException("Category with id %d does not exist");
        }
    }

    public void checkAnotherCategoryUseName(CategoryRepo categoryRepo, Long catId, String name) {
        if (categoryRepo.existsByNameAndIdNot(name, catId)) {
            throw new CategoryUniqueNameException("Name already exist");
        }
    }

    public void checkNameAlreadyExist(CategoryRepo categoryRepo, String name) {
        if (categoryRepo.existsByName(name)) {
            throw new CategoryUniqueNameException("Name already exist");
        }
    }
}
