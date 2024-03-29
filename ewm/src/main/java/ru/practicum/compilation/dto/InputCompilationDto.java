package ru.practicum.compilation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.validator.annotation.ValidationGroups;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InputCompilationDto {
    private List<Long> events = List.of();
    private Boolean pinned;
    @NotBlank(groups = ValidationGroups.Create.class)
    @Size(max = 50)
    private String title;
}
