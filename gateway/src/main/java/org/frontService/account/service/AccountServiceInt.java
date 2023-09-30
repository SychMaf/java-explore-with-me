package org.frontService.account.service;

import org.frontService.account.model.AccountUser;
import org.springframework.web.multipart.MultipartFile;

public interface AccountServiceInt {
    String registeredAccount(AccountUser accountUser, MultipartFile file);
}
