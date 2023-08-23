package ru.practicum.events.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.category.dto.OutputCategoryDto;
import ru.practicum.user.dto.OutputUserDto;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShortOutputEventDto {
    private String annotation;
    private OutputCategoryDto category;
    private Long confirmedRequests;
    private Long id;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    private OutputUserDto initiator;
    private Boolean paid;
    private String title;
    private Long views;
}
