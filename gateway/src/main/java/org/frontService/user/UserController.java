package org.frontService.user;

import org.frontService.user.dto.InputUserDto;
import org.frontService.user.dto.OutputUserDto;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Controller
public class UserController {
    protected final RestTemplate rest = new RestTemplate();
    private String path = "http://localhost:8080";

    @PostMapping("/createUser")
    public String createUser(@ModelAttribute InputUserDto inputUserDto, Model model) {
        try {
            ResponseEntity<OutputUserDto> dto = rest.postForEntity(path + "/admin/users", new HttpEntity<>(inputUserDto), OutputUserDto.class);
            model.addAttribute("dto", Objects.requireNonNull(dto.getBody()));
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
        return "user/userDelete";
    }

}
