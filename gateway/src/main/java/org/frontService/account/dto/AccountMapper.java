package org.frontService.account.dto;

import lombok.experimental.UtilityClass;
import org.frontService.account.model.AccountUser;

@UtilityClass
public class AccountMapper {
    public AccountUser patchAccountFromDto(AccountDto accountDto, AccountUser accountUser) {
        return AccountUser.builder()
                .id(accountUser.getId())
                .active(accountUser.isActive())
                .avatar(accountUser.getAvatar())
                .username(accountDto.getUsername().isEmpty() ?
                        accountUser.getUsername() : accountDto.getUsername())
                .password(accountDto.getPassword().isEmpty() ?
                        accountUser.getPassword() : accountDto.getPassword())
                .build();
    }
}
