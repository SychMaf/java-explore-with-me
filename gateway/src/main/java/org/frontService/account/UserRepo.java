package org.frontService.account;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<AccountUser, Long> {
    AccountUser findByUsername(String username);
}
