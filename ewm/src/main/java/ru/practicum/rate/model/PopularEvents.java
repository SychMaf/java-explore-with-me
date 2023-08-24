package ru.practicum.rate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.events.model.Event;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PopularEvents {
    private Event event;
    private Double rating;
    private Long countLike;
    private Long countDislike;
}
