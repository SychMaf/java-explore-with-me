package ru.practicum.compilation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.compilation.dto.CompilationMapper;
import ru.practicum.compilation.dto.InputCompilationDto;
import ru.practicum.compilation.dto.OutputCompilationDto;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.compilation.repo.CompilationCriteria;
import ru.practicum.compilation.repo.CompilationRepo;
import ru.practicum.compilation.repo.CompilationSpecification;
import ru.practicum.events.model.Event;
import ru.practicum.events.repo.EventRepo;
import ru.practicum.exception.exceptions.NotFoundException;
import ru.practicum.validator.CompilationValidator;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {
    private final EventRepo eventRepo;
    private final CompilationRepo compilationRepo;

    @Override
    @Transactional
    public OutputCompilationDto createCompilation(InputCompilationDto inputCompilationDto) {
        List<Event> events = eventRepo.findAllByIdIn(inputCompilationDto.getEvents());
        Compilation compilation = CompilationMapper.inputCompilationDtotoCompilation(inputCompilationDto, events);
        return CompilationMapper.compilationToOutputCompilationDto(compilationRepo.save(compilation));
    }

    @Override
    @Transactional
    public void deleteCompilation(Long compId) {
        CompilationValidator.checkCompilationExist(compilationRepo, compId);
        compilationRepo.deleteById(compId);
    }

    @Override
    @Transactional
    public OutputCompilationDto patchCompilation(Long compId, InputCompilationDto inputCompilationDto) {
        Compilation updateComp = compilationRepo.findById(compId)
                .orElseThrow(() -> new NotFoundException("Compilation with id %d does not exist"));
        List<Event> events = eventRepo.findAllByIdIn(inputCompilationDto.getEvents());
        updateComp = CompilationMapper.updateCompilation(inputCompilationDto, updateComp, events);
        return CompilationMapper.compilationToOutputCompilationDto(compilationRepo.save(updateComp));
    }

    @Override
    @Transactional(readOnly = true)
    public OutputCompilationDto searchCompilationById(Long compId) {
        return CompilationMapper.compilationToOutputCompilationDto(compilationRepo.findById(compId)
                .orElseThrow(() -> new NotFoundException("Compilation with id %d does not exist")));
    }

    @Override
    @Transactional(readOnly = true)
    public List<OutputCompilationDto> searchCompilation(Boolean pinned, Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from / size, size);
        CompilationCriteria criteria = CompilationCriteria.builder()
                .pinned(pinned)
                .build();
        CompilationSpecification compilationSpecification = new CompilationSpecification(criteria);
        return compilationRepo.findAll(compilationSpecification, pageable).stream()
                .map(CompilationMapper::compilationToOutputCompilationDto)
                .collect(Collectors.toList());
    }
}
