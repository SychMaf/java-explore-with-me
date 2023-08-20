package ru.practicum.compilation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.compilation.dto.InputCompilationDto;
import ru.practicum.compilation.dto.OutputCompilationDto;
import ru.practicum.compilation.service.CompilationService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/compilations")
@Slf4j
public class CompilationAdminController {
    private final CompilationService compilationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OutputCompilationDto createCompilation(@RequestBody InputCompilationDto inputCompilationDto) {
        log.trace("ADMIN CONTROLLER: request to create compilation: {}", inputCompilationDto);
        return compilationService.createCompilation(inputCompilationDto);
    }

    @DeleteMapping("/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)  //204
    public void deleteCompilation(@PathVariable Long compId) {
        log.trace("ADMIN CONTROLLER: request to delete compilation with Id: {}", compId);
        compilationService.deleteCompilation(compId);
    }

    @PatchMapping("/{compId}")
    @ResponseStatus(HttpStatus.OK)
    public OutputCompilationDto patchCompilation(@PathVariable Long compId,
                                                 @RequestBody InputCompilationDto inputCompilationDto) {
        log.trace("ADMIN CONTROLLER: request to patch compilation with Id: {}", compId);
        return compilationService.patchCompilation(compId, inputCompilationDto);
    }
}
