package ru.practicum.events.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.category.dto.OutputCategoryDto;
import ru.practicum.category.model.Category;
import ru.practicum.events.model.Location;
import ru.practicum.user.dto.OutputUserDto;
import ru.practicum.user.model.User;

import java.time.LocalDateTime;

@Data
@Builder
public class ShortOutputEventDto {
    private String annotation;
    private OutputCategoryDto category;
    private Long confirmedRequest;
    private Long id;
    private LocalDateTime eventDate;
    private OutputUserDto initiator;
    private Boolean paid;
    private String title;
    private Long views;
}
