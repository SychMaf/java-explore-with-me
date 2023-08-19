package ru.practicum.events.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.events.dto.FullOutputEventDto;
import ru.practicum.events.dto.InputEventDto;
import ru.practicum.events.dto.ShortOutputEventDto;
import ru.practicum.events.dto.UpdateUserEventDto;
import ru.practicum.events.service.EventsService;
import ru.practicum.requests.dto.OutputRequestDto;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@Slf4j
public class UserEventsController {
    private final EventsService eventsService;

    @GetMapping("/{userId}/events")
    @ResponseStatus(HttpStatus.OK)
    public List<ShortOutputEventDto> getUserEvents(@PathVariable Long userId,
                                                   @RequestParam(required = false, defaultValue = "0") @PositiveOrZero Integer from,
                                                   @RequestParam(required = false, defaultValue = "10") @Positive Integer size) {
        log.trace("CONTROLLER: request to get events created user with id: {}", userId);
        return eventsService.getUserEvents(userId, from, size);
    }

    @PostMapping("/{userId}/events")
    @ResponseStatus(HttpStatus.CREATED)
    public FullOutputEventDto addEvent(@PathVariable Long userId,
                                       @RequestBody InputEventDto inputEventDto) {
        log.trace("CONTROLLER: request to create user: {}", inputEventDto);
        return eventsService.addEvent(userId, inputEventDto);
    }

    @GetMapping("/{userId}/events/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public FullOutputEventDto getEventById(@PathVariable Long userId,
                                           @PathVariable Long eventId) {
        log.trace("CONTROLLER: request to find event with id: {}", eventId);
        return eventsService.getUserEventById(userId, eventId);
    }

    @PatchMapping("/{userId}/events/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public FullOutputEventDto patchEvent(@PathVariable Long userId,
                                         @PathVariable Long eventId,
                                         @RequestBody UpdateUserEventDto updateUserEventDto) {
        log.trace("CONTROLLER: request to update event: {}", updateUserEventDto);
        return eventsService.patchEvent(userId, eventId, updateUserEventDto);
    }


    //ДВА ПОИНТА НА РЕКВЕСТЫ!!!

}
