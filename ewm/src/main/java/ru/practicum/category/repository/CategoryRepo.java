package ru.practicum.category.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.category.model.Category;

public interface CategoryRepo extends JpaRepository<Category, Long> {
    Boolean existsByName(String name);

    Boolean existsByNameAndIdNot(String name, Long catId);
}
