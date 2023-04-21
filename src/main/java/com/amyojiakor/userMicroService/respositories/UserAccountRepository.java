package com.amyojiakor.userMicroService.respositories;

import com.amyojiakor.userMicroService.models.entities.User;
import com.amyojiakor.userMicroService.models.entities.UserAccounts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserAccountRepository extends JpaRepository<UserAccounts, Long> {
    Optional<UserAccounts> findByAccountNumber(String accountNumber);
}
