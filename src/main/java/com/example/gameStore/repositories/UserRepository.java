package com.example.gameStore.repositories;

import com.example.gameStore.entities.User;
import com.example.gameStore.enums.UserRole;
import com.example.gameStore.enums.UserStatus;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    @Modifying
    @Transactional
    @Query("UPDATE User SET activeStatus=:status WHERE id =:userId AND activeStatus <> :status")
    int updateUserStatus(@Param("userId") UUID userId, @Param("status") UserStatus status);

    @Modifying
    @Transactional
    @Query("UPDATE User SET role=:role WHERE id =:userId AND role <> :role ")
    int updateUserRole(@Param("userId") UUID userId, @Param("role") UserRole role);

    Optional<User> findByEmail(String email);

    Optional<User> findByPasswordResetToken(String token);

    Optional<User> findByConfirmEmailToken(String token);
}
