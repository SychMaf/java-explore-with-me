package ru.practicum.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.dto.InputDto;
import ru.practicum.dto.OutputDto;
import ru.practicum.model.Stats;

@UtilityClass
public class StatDtoMapper {

    public Stats toStatFromDto(InputDto inputDto) {
        return Stats.builder()
                .app(inputDto.getApp())
                .uri(inputDto.getUri())
                .ip(inputDto.getIp())
                .requestTime(inputDto.getTimestamp())
                .build();
    }

    public OutputDto toOutputDto(Stats stats, Long hits) {
        return OutputDto.builder()
                .app(stats.getApp())
                .uri(stats.getUri())
                .hits(hits)
                .build();
    }
}
