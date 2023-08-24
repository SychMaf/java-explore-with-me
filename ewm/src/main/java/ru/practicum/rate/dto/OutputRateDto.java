package ru.practicum.rate.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.rate.model.StateRate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OutputRateDto {
    private Long userId;
    private Long eventId;
    private StateRate rate;
    private Long countLike;
    private Long countDislike;
}
