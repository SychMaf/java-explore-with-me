package ru.practicum.rate.service;

import ru.practicum.rate.dto.OutputRateDto;
import ru.practicum.rate.dto.PopularEventsDto;

import java.util.List;

public interface RateService {
    OutputRateDto createLike(Long userId, Long eventId, String rate);

    void deleteLike(Long userId, Long eventId);

    List<PopularEventsDto> getMustPopularEvents(Integer from, Integer size);
}