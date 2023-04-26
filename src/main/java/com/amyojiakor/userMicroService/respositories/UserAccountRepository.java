package com.amyojiakor.userMicroService.respositories;

import com.amyojiakor.userMicroService.models.entities.UserAccounts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserAccountRepository extends JpaRepository<UserAccounts, Long> {
    Optional<UserAccounts> findByAccountNumber(String accountNumber);
//    Optional<UserAccounts> findALlByUser(long userId);

//    @Query(value="SELECT * FROM accounts a WHERE a.user_id = :userId",nativeQuery = true)
//    Optional<UserAccounts> findAllByUserId(@Param("userId")long userId);


}
