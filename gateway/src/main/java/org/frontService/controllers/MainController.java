package org.frontService.controllers;


import org.frontService.user.dto.InputUserDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class MainController {

    @GetMapping("/homePage")
    public String getHomePage() {
        return "home";
    }

    @GetMapping("/userCreateForm")
    public String getUserOperation(@ModelAttribute InputUserDto inputUserDto, Model model) {
        model.addAttribute(inputUserDto);
        return "user/userCreateForm";
    }


}
