package ru.practicum.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.exception.exceptions.UserEmailConflictException;
import ru.practicum.user.dto.InputUserDto;
import ru.practicum.user.dto.OutputUserDto;
import ru.practicum.user.dto.UserDtoMapper;
import ru.practicum.user.repository.UserCriteria;
import ru.practicum.user.repository.UserRepo;
import ru.practicum.user.repository.UserSpecification;
import ru.practicum.validator.UserValidator;

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
            throw new UserEmailConflictException("Email already used");
        }
        return UserDtoMapper.toOutputUserDto(userRepo.save(UserDtoMapper.toUser(inputUserDto)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<OutputUserDto> getUsers(List<Long> ids, Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from / size, size);
        UserCriteria criteria = UserCriteria.builder()
                .ids(ids)
                .build();
        UserSpecification userSpecification = new UserSpecification(criteria);
        return userRepo.findAll(userSpecification, pageable).stream()
                .map(UserDtoMapper::toOutputUserDto)
                .collect(Collectors.toList());

    }

    @Override
    @Transactional
    public void deleteUser(Long userId) {
        UserValidator.checkUserExist(userRepo, userId);
        userRepo.deleteById(userId);
    }
}
