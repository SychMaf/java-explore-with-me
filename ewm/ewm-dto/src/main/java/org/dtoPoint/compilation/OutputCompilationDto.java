package org.dtoPoint.compilation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.dtoPoint.events.ShortOutputEventDto;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OutputCompilationDto {
    private List<ShortOutputEventDto> events;
    private Boolean pinned;
    private String title;
    private Long id;
}
