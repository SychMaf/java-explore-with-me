package ru.practicum.rate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import org.springframework.web.bind.annotation.*;
import ru.practicum.rate.dto.OutputRateDto;
import ru.practicum.rate.dto.PopularEventsDto;
import ru.practicum.rate.service.RateService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/like")
@Slf4j
public class RateController {
    private final RateService rateService;

    @PostMapping("/{userId}/events/{eventId}")
    @ResponseStatus(HttpStatus.CREATED) //201
    public OutputRateDto createLike(@PathVariable Long userId,
                                    @PathVariable Long eventId,
                                    @RequestParam String rate) {
        log.trace("RATE CONTROLLER: request to CREATE Like from user id: {}, event id:{}", userId, eventId);
        return rateService.createLike(userId, eventId, rate);
    }

    @DeleteMapping("/{userId}/events/{eventId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)  //204
    public void deleteLike(@PathVariable Long userId,
                           @PathVariable Long eventId) {
        log.trace("RATE CONTROLLER: request to DELETE Like from user id: {}, event id:{}", userId, eventId);
        rateService.deleteLike(userId, eventId);
    }

    @GetMapping("/top/events/")
    @ResponseStatus(HttpStatus.OK)
    public List<PopularEventsDto> getMustPopularEvents(@RequestParam(required = false, defaultValue = "0") @PositiveOrZero Integer from,
                                                       @RequestParam(required = false, defaultValue = "10") @Positive Integer size) {
        log.trace("RATE CONTROLLER: request to Find most popular events");
        return rateService.getMustPopularEvents(from, size);
    }
}
