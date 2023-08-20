package ru.practicum.compilation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.events.dto.ShortOutputEventDto;

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
