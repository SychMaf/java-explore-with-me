package ru.practicum.events.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.dtoPoint.events.FullOutputEventDto;
import org.dtoPoint.events.ShortOutputEventDto;
import ru.practicum.events.service.EventsService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/events")
@Slf4j
public class PublicEventsController {
    private final EventsService eventsService;
    private final HttpServletRequest servletRequest;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ShortOutputEventDto> searchEventsWithParam(@RequestParam(required = false) String text,
                                                           @RequestParam(required = false) List<Integer> categories,
                                                           @RequestParam(required = false) Boolean paid,
                                                           @RequestParam(name = "rangeStart", required = false)
                                                           @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                                           @RequestParam(name = "rangeEnd", required = false)
                                                           @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                                           @RequestParam(required = false) Boolean onlyAvailable,
                                                           @RequestParam(required = false) String sort,
                                                           @RequestParam(required = false, defaultValue = "0") @PositiveOrZero Integer from,
                                                           @RequestParam(required = false, defaultValue = "10") @Positive Integer size) {
        log.trace("PUBLIC CONTROLLER: request to find events with a lot params...");
        return eventsService.searchEventsWithParam(text,
                categories,
                paid,
                rangeStart,
                rangeEnd,
                onlyAvailable,
                sort,
                from,
                size,
                servletRequest.getRemoteAddr());
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public FullOutputEventDto getEventById(@PathVariable Long id) {
        log.trace("PUBLIC CONTROLLER: request to find event with id: {}", id);
        return eventsService.getEventById(id, servletRequest.getRemoteAddr());
    }
}
