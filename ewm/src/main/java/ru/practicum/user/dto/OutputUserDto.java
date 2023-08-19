package ru.practicum.user.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OutputUserDto {
    private String name;
    private String email;
    private Long id;
}
