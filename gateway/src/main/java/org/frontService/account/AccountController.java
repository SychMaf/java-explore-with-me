package org.frontService.account;

import lombok.RequiredArgsConstructor;
import org.frontService.account.dto.AccountDto;
import org.frontService.account.model.AccountUser;
import org.frontService.account.service.AccountService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @GetMapping("/registration")
    public String getRegistration(@ModelAttribute AccountUser accountUser, Model model) {
        model.addAttribute(accountUser);
        return "registration";
    }

    @PostMapping("/registration")
    public String setRegistration(@ModelAttribute AccountUser accountUser,
                                  @RequestParam("file") MultipartFile file) {
        return accountService.registeredAccount(accountUser, file);
    }

    @GetMapping("/backrooms")
    public String getLK(@AuthenticationPrincipal AccountUser user, Model model) {
        model.addAttribute("user", user);
        return "LK";
    }

    @GetMapping("/account/patch")
    public String updateAccount(@ModelAttribute AccountDto accountDto, Model model) {
        model.addAttribute("patchAccount", accountDto);
        return "account/patchAccountForm";
    }

    @PostMapping("/account/patch")
    public String updateAccount(@AuthenticationPrincipal AccountUser user,
                                @ModelAttribute AccountDto accountDto,
                                @RequestParam("file") MultipartFile file,
                                Model model) {
        try {
            model.addAttribute("user", accountService.patchAccount(user, accountDto, file));
        } catch (RuntimeException e) {
            return "exception/nameException";
        }
        return "LK";
    }
}
