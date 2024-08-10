package com.example.gameStore.repositories;


import com.example.gameStore.dtos.GameDtos.SingleGameWithReviewsQueryDto;
import com.example.gameStore.entities.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface GameRepository extends JpaRepository<Game, UUID> {

    @Query("""
            SELECT new com.example.gameStore.dtos.GameDtos.SingleGameWithReviewsQueryDto(
                g.id, g.name, g.quantity, g.thumbnail, g.images, g.developer, g.releaseDate,
                g.systemRequirements, g.price, g.description, g.sku, g.rating, g.isActive,
                g.genreList, g.playerSupport, r.id, r.description, r.starRating, r.createdAt, u.username
            )
            FROM Review r
            JOIN r.gameId g
            JOIN r.userId u
            WHERE g.id = :gameId
            """)
    Optional<List<SingleGameWithReviewsQueryDto>> getSingleGame(@Param("gameId") UUID gameId);

    @Query(value = """
            SELECT COUNT(*)
            FROM t_key
            WHERE game_id = :gameId
            """, nativeQuery = true)
    Optional<Integer> getGameKeysAmount(@Param("gameId") UUID gameId);

    @Query(value = """
            SELECT unnest(enum_range(NULL::e_game_genre))
            """, nativeQuery = true)
    Optional<List<String>> getAllGenresList();

    @Query(value = """
            SELECT t_game.*
            FROM t_game
            JOIN genres ON t_game.id = genres.genre_value
            WHERE genres.genre = :genre
            """, nativeQuery = true)
    Optional<List<Game>> getGamesByGenre(@Param("genre") String genre);
}


