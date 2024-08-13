package com.example.gameStore.repositories;

import com.example.gameStore.entities.Game;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface GameRepository extends JpaRepository<Game, UUID> {

    Page<Game> findAllByIsActiveTrue(Pageable pageable);

    @Query(value = """
            SELECT COUNT(*)
            FROM t_key
            WHERE game_id = :gameId
            """, nativeQuery = true)
    Optional<Integer> getGameKeysAmount(@Param("gameId") UUID gameId);

    @Query(value = """
            SELECT unnest(enum_range(NULL::e_game_genre))
            """, nativeQuery = true)
    List<String> getAllGenresList();

    @Query(value = """
            SELECT t_game.*
            FROM t_game
            JOIN genres ON t_game.id = genres.genre_value
            WHERE genres.genre = :genre
            """, nativeQuery = true)
    List<Game> getGamesByGenre(@Param("genre") String genre);
}


