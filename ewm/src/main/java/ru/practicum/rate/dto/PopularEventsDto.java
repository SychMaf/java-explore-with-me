package ru.practicum.rate.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.events.dto.ShortOutputEventDto;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PopularEventsDto {
    private ShortOutputEventDto shortOutputEventDto;
    private Double rating;
    private Long countLike;
    private Long countDislike;
}
