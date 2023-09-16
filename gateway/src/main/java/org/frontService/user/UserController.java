package org.frontService.user;

import lombok.RequiredArgsConstructor;
import org.dtoPoint.user.InputUserDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Objects;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserClient userClient;

    @GetMapping("/userCreateForm")
    public String getUserOperation(@ModelAttribute InputUserDto inputUserDto, Model model) {
        model.addAttribute(inputUserDto);
        return "user/userCreateForm";
    }

    @PostMapping("/createUser")
    public String createUser(@ModelAttribute InputUserDto inputUserDto, Model model) {
        try {
            model.addAttribute("dto", userClient.saveUser(inputUserDto));
        } catch (HttpClientErrorException e) {
            if (Objects.requireNonNull(e.getMessage()).contains("Email already used")) {
                return "/exception/emailException";
            }
        }
        return "user/userCreate";
    }

    @GetMapping("/findUserParams")
    public String findUsers() {
        return "user/findUserParams";
    }

    @GetMapping("/deleteUserParams")
    public String deleteUsers() {
        return "user/userDeleteForm";
    }

    @PostMapping("/deleteUserParams")
    public String continueDelete(@RequestParam Long userId) {
        userClient.deleteUser(userId);
        return "user/userDelete";
    }

}
