package org.dtoPoint.rate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
