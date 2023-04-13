package com.amyojiakor.userMicroService.respositories;

import com.amyojiakor.userMicroService.models.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
