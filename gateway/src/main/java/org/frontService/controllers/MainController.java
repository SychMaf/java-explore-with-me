package org.frontService.controllers;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/homePage")
    public String getHomePage() {
        return "home";
    }



}
