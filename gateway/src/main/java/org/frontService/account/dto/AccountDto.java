package org.frontService.account.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class AccountDto {
    @Email
    @NotBlank
    @Size(min = 3, max = 254)
    private String username;
    @NotBlank
    private String password;
}
