package ru.practicum.rate.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.events.dto.EventMapper;
import ru.practicum.rate.model.PopularEvents;
import ru.practicum.rate.model.Rate;
import ru.practicum.rate.model.StateRate;

@UtilityClass
public class RateMapper {
    public OutputRateDto toOutputRateDto(Rate rate, Long like, Long dislike) {
        return OutputRateDto.builder()
                .eventId(rate.getRatePK().getEvent().getId())
                .userId(rate.getRatePK().getUser().getId())
                .rate(rate.getRate() != 0 ? StateRate.LIKE : StateRate.DISLIKE)
                .countLike(like)
                .countDislike(dislike)
                .build();
    }

    public PopularEventsDto toPopularEventsDto(PopularEvents popularEvents) {
        return PopularEventsDto.builder()
                .shortOutputEventDto(EventMapper.eventToShortOutputDto(popularEvents.getEvent()))
                .countLike(popularEvents.getCountLike())
                .countDislike(popularEvents.getCountDislike())
                .rating(popularEvents.getRating())
                .build();
    }
}
