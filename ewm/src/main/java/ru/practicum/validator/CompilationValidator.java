package ru.practicum.validator;

import lombok.experimental.UtilityClass;
import ru.practicum.compilation.repo.CompilationRepo;
import ru.practicum.exception.exceptions.NotFoundException;

@UtilityClass
public class CompilationValidator {
    public void checkCompilationExist(CompilationRepo compilationRepo, Long compId) {
        if (!compilationRepo.existsById(compId)) {
            throw new NotFoundException("Compilation with id %d does not exist");
        }
    }
}
