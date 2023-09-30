package ru.practicum.rate.service;

import org.dtoPoint.rate.OutputRateDto;
import org.dtoPoint.rate.PopularEventsDto;

import java.util.List;

public interface RateService {
    OutputRateDto createLike(Long userId, Long eventId, String rate);

    void deleteLike(Long userId, Long eventId);

    List<PopularEventsDto> getMustPopularEvents(Integer from, Integer size);
}