package ru.practicum.user.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class InputUserDto {
    @NotBlank
    @Size(min = 2)
    @Size(max = 250)
    private String name;
    @NotBlank
    @Email
    private String email;
}
