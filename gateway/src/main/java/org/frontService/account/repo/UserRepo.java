package org.frontService.account.repo;

import org.frontService.account.model.AccountUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<AccountUser, Long> {
    AccountUser findByUsername(String username);

    Boolean existsByUsername(String username);
}
