package org.dtoPoint.events;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.dtoPoint.category.OutputCategoryDto;
import org.dtoPoint.user.OutputUserDto;

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
    private Long countLikes;
    private Long countDislikes;
}
