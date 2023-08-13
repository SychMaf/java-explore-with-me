package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.InputDto;
import ru.practicum.dto.OutputDto;
import ru.practicum.service.StatService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping
public class StatController {
    private final StatService statService;

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public void addNewRequest(@RequestBody InputDto inputDto) {
        log.trace("STAT SERVICE: request to add new event: {}", inputDto);
        statService.addNewRequest(inputDto);
    }

    @GetMapping("/stats")
    @ResponseStatus(HttpStatus.OK)
    public List<OutputDto> getStats(@RequestParam(name = "start")
                                    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
                                    @RequestParam(name = "end")
                                    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
                                    @RequestParam(name = "uris", defaultValue = "") List<String> uris,
                                    @RequestParam(name = "unique", defaultValue = "false") Boolean unique) {
        log.trace("STAT SERVICE: request to get state with params: start {}; \n end {}; \n uris {};", start, end, uris);
        return statService.getStats(start, end, uris, unique);
    }
}
