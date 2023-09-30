package ru.practicum.user.service;

import org.dtoPoint.user.InputUserDto;
import org.dtoPoint.user.OutputUserDto;

import java.util.List;

public interface UserService {
    OutputUserDto saveUser(InputUserDto inputUserDto);

    List<OutputUserDto> getUsers(List<Long> ids, Integer from, Integer size);

    void deleteUser(Long userId);
}
