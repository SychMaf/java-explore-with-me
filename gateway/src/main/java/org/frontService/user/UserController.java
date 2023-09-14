package org.frontService.user;

import lombok.RequiredArgsConstructor;
import org.frontService.user.dto.InputUserDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Controller
@RequiredArgsConstructor
public class UserController {
    private String path = "http://localhost:8080";
    private final UserClient userClient;
    protected final RestTemplate rest = new RestTemplate();


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
    public String findUsers(Model model) {
        return "user/findUserParams";
    }

    @GetMapping("/deleteUserParams")
    public String deleteUsers(Model model) {
        return "user/userDeleteForm";
    }

    @PostMapping("/deleteUserParams")
    public String continueDelete(@RequestParam Long userId, Model model) {
        rest.delete(path + "/admin/users/" + userId);
        //delete("/" + id);
        return "user/userDelete";
    }

}
