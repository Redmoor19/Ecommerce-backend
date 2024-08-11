package com.example.gameStore.repositories;

import com.example.gameStore.entities.Key;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface KeyRepository extends JpaRepository<Key, UUID> {
    List<Key> findByGameId(UUID gameId);
}
