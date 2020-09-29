package com.travada.backend.module.user.repository;

import com.travada.backend.module.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("userRepository")
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);

    Optional<User> findByUsername(String username);

    User findByConfirmationCode(String confirmationCode);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    User findBynoHp(String noHp);

    List<User> findByStatus(String status);
}
