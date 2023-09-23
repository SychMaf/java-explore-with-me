package org.frontService.account.service;

import lombok.RequiredArgsConstructor;
import org.frontService.account.dto.AccountDto;
import org.frontService.account.dto.AccountMapper;
import org.frontService.account.model.AccountUser;
import org.frontService.account.model.Role;
import org.frontService.account.repo.UserRepo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountService implements UserDetailsService, AccountServiceInt {
    private final UserRepo userRepo;
    @Value("${upload.path}")
    private String path;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepo.findByUsername(username);
    }


    @Override
    public String registeredAccount(AccountUser accountUser, MultipartFile file) {
        AccountUser user = userRepo.findByUsername(accountUser.getUsername());
        if (user != null) {
            return "account/registration";
        }
        accountUser.setAvatar("Empty.png");
        if (file != null && !file.getOriginalFilename().isEmpty()) {
            accountUser.setAvatar(loadAvatar(file));
        }
        accountUser.setActive(true);
        accountUser.setRoles(Collections.singleton(Role.USER));
        userRepo.save(accountUser);
        return "home";
    }

    public AccountUser patchAccount(AccountUser accountUser, AccountDto accountDto, MultipartFile file) {
        if (userRepo.existsByUsername(accountDto.getUsername())) {
            throw new RuntimeException("Username already used");
        }
        if (file != null && !file.getOriginalFilename().isEmpty()) {
            accountUser.setAvatar(loadAvatar(file));
        }
        AccountUser patchAccount = AccountMapper.patchAccountFromDto(accountDto, accountUser);
        Authentication authentication = new UsernamePasswordAuthenticationToken(patchAccount, patchAccount.getPassword(), patchAccount.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return userRepo.save(patchAccount);
    }

    private String loadAvatar(MultipartFile file) {
        try {
            File uploadDir = new File(path);
            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }
            String uuidFile = UUID.randomUUID().toString();
            String resultFileName = uuidFile + "." + file.getOriginalFilename();
            file.transferTo(new File(path + "/" + resultFileName));
            return resultFileName;
        } catch (IOException e) {
            throw new RuntimeException("Ошибка создания аватара пользователя");
        }
    }
}
