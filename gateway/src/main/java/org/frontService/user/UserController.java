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

    @GetMapping("/create/user")
    public String createUserForm(@ModelAttribute InputUserDto inputUserDto, Model model) {
        model.addAttribute(inputUserDto);
        return "user/userCreateForm";
    }

    @PostMapping("/create/user")
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

    @GetMapping("/find/user/params")
    public String findUser() {
        return "user/findUserParams";
    }

    @GetMapping("/delete/user")
    public String deleteUser() {
        return "user/userDeleteForm";
    }

    @PostMapping("/delete/user")
    public String deleteUser(@RequestParam Long userId) {
        userClient.deleteUser(userId);
        return "user/userDelete";
    }

}
