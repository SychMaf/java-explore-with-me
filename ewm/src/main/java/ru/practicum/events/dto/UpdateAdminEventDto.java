package ru.practicum.events.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import org.aspectj.lang.annotation.Before;
import ru.practicum.events.model.Location;
import ru.practicum.validator.EventStartBefore;
import ru.practicum.validator.ValidationGroups;

import javax.validation.constraints.Future;
import java.time.LocalDateTime;

@Data
@Builder
public class UpdateAdminEventDto {
    private String annotation;
    private Long category;
    private String description;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    private Location location;
    private Boolean paid;
    private Long participantLimit;
    private Boolean requestModeration;
    private String stateAction;
    private String title;
}
