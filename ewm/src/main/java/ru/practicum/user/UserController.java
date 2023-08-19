package ru.practicum.user;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.user.dto.InputUserDto;
import ru.practicum.user.dto.OutputUserDto;
import ru.practicum.user.service.UserService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;

    @PostMapping("/admin/users")
    @ResponseStatus(HttpStatus.CREATED) //201
    public OutputUserDto saveUser(@RequestBody InputUserDto inputUserDto) {
        log.trace("CONTROLLER: request to create user: {}", inputUserDto);
        return userService.saveUser(inputUserDto);
    }

    @GetMapping("/admin/users")
    @ResponseStatus(HttpStatus.OK)  //200
    public List<OutputUserDto> getUsers(@RequestParam List<Long> ids,
                                        @RequestParam(required = false, defaultValue = "0") @PositiveOrZero Integer from,
                                        @RequestParam(required = false, defaultValue = "10") @Positive Integer size) {
        log.trace("CONTROLLER: request to find users");
        return userService.getUsers(ids, from, size);
    }

    @DeleteMapping("/admin/users/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)  //204
    public void deleteUser(@PathVariable Long userId) {
        log.trace("CONTROLLER: request to delete users");
        userService.deleteUser(userId);
    }

}
