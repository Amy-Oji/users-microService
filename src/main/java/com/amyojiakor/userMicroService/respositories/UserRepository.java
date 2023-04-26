package com.amyojiakor.userMicroService.respositories;

import com.amyojiakor.userMicroService.models.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
//    @Query("SELECT u FROM User u JOIN FETCH u.userAccounts WHERE u.id = :userId")
//    User findUserWithAccounts(@Param("userId") Long userId);
}
