package ru.practicum.validator;

import lombok.experimental.UtilityClass;
import ru.practicum.exception.exceptions.NotFoundException;
import ru.practicum.user.repository.UserRepo;

@UtilityClass
public class UserValidator {
    public void checkUserExist(UserRepo userRepo, Long userId) {
        if (!userRepo.existsById(userId)) {
            throw new NotFoundException("User with id %d does not exist");
        }
    }
}
