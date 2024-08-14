package com.example.gameStore.repositories;

import com.example.gameStore.entities.Game;
import com.example.gameStore.enums.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface GameRepository extends JpaRepository<Game, UUID>, JpaSpecificationExecutor<Game> {

    @Query(value = """
            SELECT COUNT(*)
            FROM key
            WHERE game_id = :gameId
            """, nativeQuery = true)
    Optional<Integer> getGameKeysAmount(@Param("gameId") UUID gameId);

    @Query(value = """
            SELECT unnest(enum_range(NULL::e_game_genre))
            """, nativeQuery = true)
    List<String> getAllGenresList();

    @Query(value = """
            SELECT unnest(enum_range(NULL::e_player_support))
            """, nativeQuery = true)
    List<String> getAllPlayerSupportList();

    @Query("SELECT g FROM Game g JOIN g.genreList ge WHERE ge = :genre")
    List<Game> getGamesByGenre(@Param("genre") Genre genre);

    @Query(value = """
            SELECT game.*
            FROM game
            JOIN player_support ON game.id = player_support.player_support_value
            WHERE player_support.player_support = :playerSupport
            """, nativeQuery = true)
    List<Game> getGamesByPlayerSupport(@Param("playerSupport") String playerSupport);
}


