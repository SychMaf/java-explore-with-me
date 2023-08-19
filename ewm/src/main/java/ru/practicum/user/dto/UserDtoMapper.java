package ru.practicum.user.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.user.model.User;

@UtilityClass
public class UserDtoMapper {
    public User toUser(InputUserDto inputUserDto) {
        return User.builder()
                .name(inputUserDto.getName())
                .email(inputUserDto.getEmail())
                .build();
    }

    public OutputUserDto toOutputUserDto(User user) {
        return OutputUserDto.builder()
                .name(user.getName())
                .email(user.getEmail())
                .id(user.getId())
                .build();
    }
}
