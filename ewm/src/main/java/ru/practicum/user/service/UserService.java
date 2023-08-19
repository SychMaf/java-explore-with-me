package ru.practicum.user.service;

import ru.practicum.user.dto.InputUserDto;
import ru.practicum.user.dto.OutputUserDto;

import java.util.List;

public interface UserService {
    OutputUserDto saveUser(InputUserDto inputUserDto);

    List<OutputUserDto> getUsers(List<Long> ids, Integer from, Integer size);

    void deleteUser(Long userId);
}
