package com.example.gameStore.repositories;


import com.example.gameStore.entities.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface GameRepository extends JpaRepository<Game, UUID> {

    @Query(value = """
            SELECT COUNT(*)
            FROM t_key
            WHERE game_id = :gameId
            """, nativeQuery = true)
    Optional<Integer> getGameKeysAmount(@Param("gameId") UUID gameId);
}


