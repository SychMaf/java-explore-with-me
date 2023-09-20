package org.frontService.controllers;


import lombok.RequiredArgsConstructor;
import org.frontService.account.AccountUser;
import org.frontService.account.Role;
import org.frontService.account.UserRepo;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Collections;

@Controller
@RequiredArgsConstructor
public class MainController {
    private final UserRepo userRepo;

    @GetMapping("/homePage")
    public String getHomePage() {
        return "home";
    }

    @GetMapping("/registration")
    public String getRegistration(@ModelAttribute AccountUser accountUser, Model model) {
        model.addAttribute(accountUser);
        return "registration";
    }

    @PostMapping("/registration")
    public String setRegistration(@ModelAttribute AccountUser accountUser, Model model) {
        AccountUser user = userRepo.findByUsername(accountUser.getUsername());
        if (user != null) {
            return "registration";
        }
        accountUser.setActive(true);
        accountUser.setRoles(Collections.singleton(Role.USER));
        userRepo.save(accountUser);
        return "home";
    }

}
