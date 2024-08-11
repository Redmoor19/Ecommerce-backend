package com.example.gameStore.repositories;

import com.example.gameStore.entities.Key;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface KeyRepository extends JpaRepository<Key, UUID> {

    @Modifying
    @Transactional
    @Query(value = """
            UPDATE t_game
            SET quantity = quantity + 1
            WHERE id = :gameId
            """, nativeQuery = true)
    int incrementQuantityById(@Param("gameId") UUID gameId);
}
