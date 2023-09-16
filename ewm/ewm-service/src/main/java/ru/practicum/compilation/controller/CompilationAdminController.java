package ru.practicum.compilation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.dtoPoint.compilation.InputCompilationDto;
import org.dtoPoint.compilation.OutputCompilationDto;
import ru.practicum.compilation.service.CompilationService;
import org.dtoPoint.compilation.ValidationGroups;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/compilations")
@Validated
@Slf4j
public class CompilationAdminController {
    private final CompilationService compilationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Validated(ValidationGroups.Create.class)
    public OutputCompilationDto createCompilation(@RequestBody @Valid InputCompilationDto inputCompilationDto) {
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
    @Validated(ValidationGroups.Update.class)
    public OutputCompilationDto patchCompilation(@PathVariable Long compId,
                                                 @RequestBody @Valid InputCompilationDto inputCompilationDto) {
        log.trace("ADMIN CONTROLLER: request to patch compilation with Id: {}", compId);
        return compilationService.patchCompilation(compId, inputCompilationDto);
    }
}
