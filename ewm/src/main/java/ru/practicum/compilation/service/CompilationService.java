package ru.practicum.compilation.service;

import ru.practicum.compilation.dto.InputCompilationDto;
import ru.practicum.compilation.dto.OutputCompilationDto;

import java.util.List;

public interface CompilationService {
    OutputCompilationDto createCompilation(InputCompilationDto inputCompilationDto);

    void deleteCompilation(Long compId);

    OutputCompilationDto patchCompilation(Long compId, InputCompilationDto inputCompilationDto);

    OutputCompilationDto searchCompilationById(Long compId);

    List<OutputCompilationDto> searchCompilation(Boolean pinned, Integer from, Integer size);
}
