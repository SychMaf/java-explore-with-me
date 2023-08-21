package ru.practicum.events.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.events.dto.FullOutputEventDto;
import ru.practicum.events.dto.InputEventDto;
import ru.practicum.events.dto.ShortOutputEventDto;
import ru.practicum.events.dto.UpdateEventDto;
import ru.practicum.events.service.EventsService;
import ru.practicum.validator.annotation.EventStartBefore;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@Validated
@Slf4j
public class UserEventsController {
    private final EventsService eventsService;

    @GetMapping("/{userId}/events")
    @ResponseStatus(HttpStatus.OK)
    public List<ShortOutputEventDto> getUserEvents(@PathVariable Long userId,
                                                   @RequestParam(required = false, defaultValue = "0") @PositiveOrZero Integer from,
                                                   @RequestParam(required = false, defaultValue = "10") @Positive Integer size) {
        log.trace("USER CONTROLLER: request to get events created user with id: {}", userId);
        return eventsService.getUserEvents(userId, from, size);
    }

    @PostMapping("/{userId}/events")
    @ResponseStatus(HttpStatus.CREATED)
    public FullOutputEventDto addEvent(@PathVariable Long userId,
                                       @RequestBody @EventStartBefore(min = 2) @Valid InputEventDto inputEventDto) {
        log.trace("USER CONTROLLER: request to create user: {}", inputEventDto);
        return eventsService.addEvent(userId, inputEventDto);
    }

    @GetMapping("/{userId}/events/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public FullOutputEventDto getEventById(@PathVariable Long userId,
                                           @PathVariable Long eventId) {
        log.trace("USER CONTROLLER: request to find event with id: {}", eventId);
        return eventsService.getUserEventById(userId, eventId);
    }

    @PatchMapping("/{userId}/events/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public FullOutputEventDto patchEvent(@PathVariable Long userId,
                                         @PathVariable Long eventId,
                                         @RequestBody @EventStartBefore(min = 2) @Valid UpdateEventDto updateEventDto) {
        log.trace("USER CONTROLLER: request to update event: {}", updateEventDto);
        return eventsService.patchEvent(userId, eventId, updateEventDto);
    }
}
