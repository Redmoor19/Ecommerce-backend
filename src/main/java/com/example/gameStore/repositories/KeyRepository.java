package com.example.gameStore.repositories;

import com.example.gameStore.entities.Key;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface KeyRepository extends JpaRepository<Key, UUID> {
    List<Key> findByGameId(UUID gameId);
}
