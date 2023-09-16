package org.dtoPoint.rate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.dtoPoint.events.ShortOutputEventDto;

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
