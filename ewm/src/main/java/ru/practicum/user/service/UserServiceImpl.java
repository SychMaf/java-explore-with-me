package ru.practicum.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.exception.exceptions.ConflictException;
import ru.practicum.exception.exceptions.NotFoundException;
import ru.practicum.user.dto.InputUserDto;
import ru.practicum.user.dto.OutputUserDto;
import ru.practicum.user.dto.UserDtoMapper;
import ru.practicum.user.repository.UserRepo;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepo userRepo;

    @Override
    @Transactional
    public OutputUserDto saveUser(InputUserDto inputUserDto) {
        if (userRepo.existsByEmail(inputUserDto.getEmail())) {
            throw new ConflictException("Email already used");
        }
        return UserDtoMapper.toOutputUserDto(userRepo.save(UserDtoMapper.toUser(inputUserDto)));
    }


    //Написать поиск по спецификации
    @Override
    @Transactional(readOnly = true)
    public List<OutputUserDto> getUsers(List<Long> ids, Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from / size, size);
        if (ids != null) {
            return userRepo.findAllByIdIn(ids, pageable).stream()
                    .map(UserDtoMapper::toOutputUserDto)
                    .collect(Collectors.toList());
        } else {
            return userRepo.findAll(pageable).stream()
                    .map(UserDtoMapper::toOutputUserDto)
                    .collect(Collectors.toList());
        }
    }

    @Override
    @Transactional
    public void deleteUser(Long userId) {
        if (!userRepo.existsById(userId)) {
            throw new NotFoundException("User with id %d does not exist");
        }
        userRepo.deleteById(userId);
    }
}
