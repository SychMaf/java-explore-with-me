package ru.practicum.events.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.events.dto.FullOutputEventDto;
import ru.practicum.events.dto.UpdateAdminEventDto;
import ru.practicum.events.dto.UpdateUserEventDto;
import ru.practicum.events.service.EventsService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
@Slf4j
public class AdminEventsController {
    private final EventsService eventsService;

    @GetMapping("/events")
    @ResponseStatus(HttpStatus.OK)
    public List<FullOutputEventDto> getFullInformationEvents(@RequestParam List<Long> users,
                                                             @RequestParam List<String> states,
                                                             @RequestParam List<Long> categories,
                                                             @RequestParam(name = "rangeStart")
                                                             @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                                             @RequestParam(name = "rangeEnd")
                                                             @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                                             @RequestParam(required = false, defaultValue = "0") @PositiveOrZero Integer from,
                                                             @RequestParam(required = false, defaultValue = "10") @Positive Integer size) {
        log.trace("ADMIN CONTROLLER: request to find events with a lot params...");
        return eventsService.getFullInformationEvents(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PatchMapping("/events/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public FullOutputEventDto adminPatchEvent(@PathVariable Long eventId,
                                              @RequestBody UpdateUserEventDto updateUserEventDto) {
        log.trace("CONTROLLER: request to update category: {}", updateUserEventDto);
        return eventsService.adminPatchEvent(eventId, updateUserEventDto);
    }
}
