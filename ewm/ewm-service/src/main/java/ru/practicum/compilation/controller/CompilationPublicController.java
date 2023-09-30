package ru.practicum.compilation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.dtoPoint.compilation.OutputCompilationDto;
import ru.practicum.compilation.service.CompilationService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/compilations")
@Slf4j
public class CompilationPublicController {
    private final CompilationService compilationService;

    @GetMapping("/{compId}")
    @ResponseStatus(HttpStatus.OK)
    public OutputCompilationDto searchCompilationById(@PathVariable Long compId) {
        log.trace("PUBLIC CONTROLLER: request to find comp with id:{}", compId);
        return  compilationService.searchCompilationById(compId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<OutputCompilationDto> searchCompilation(@RequestParam(required = false) Boolean pinned,
                                                        @RequestParam(required = false, defaultValue = "0") @PositiveOrZero Integer from,
                                                        @RequestParam(required = false, defaultValue = "10") @Positive Integer size) {
        log.trace("PUBLIC CONTROLLER: request to find events with a lot params...");
        return  compilationService.searchCompilation(pinned, from, size);
    }
}
