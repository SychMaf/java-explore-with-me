package ru.practicum.compilation.dto;

import lombok.experimental.UtilityClass;
import org.dtoPoint.compilation.InputCompilationDto;
import org.dtoPoint.compilation.OutputCompilationDto;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.events.dto.EventMapper;
import ru.practicum.events.model.Event;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class CompilationMapper {

    public Compilation inputCompilationDtotoCompilation(InputCompilationDto inputCompilationDto, List<Event> events) {
        return Compilation.builder()
                .pinned(inputCompilationDto.getPinned() != null ? inputCompilationDto.getPinned() : false)
                .title(inputCompilationDto.getTitle())
                .events(events)
                .build();
    }

    public OutputCompilationDto compilationToOutputCompilationDto(Compilation compilation) {
        return OutputCompilationDto.builder()
                .pinned(compilation.getPinned())
                .title(compilation.getTitle())
                .id(compilation.getId())
                .events(compilation.getEvents().stream()
                        .map(EventMapper::eventToShortOutputDto)
                        .collect(Collectors.toList()))
                .build();
    }

    public Compilation updateCompilation(InputCompilationDto inputCompilationDto, Compilation compilation, List<Event> events) {
        return Compilation.builder()
                .title(inputCompilationDto.getTitle() != null ?
                        inputCompilationDto.getTitle() : compilation.getTitle())
                .pinned(inputCompilationDto.getPinned() != null ?
                        inputCompilationDto.getPinned() : compilation.getPinned())
                .id(compilation.getId())
                .events(events)
                .build();
    }
}
