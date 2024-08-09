package com.example.gameStore.repositories;


import com.example.gameStore.dtos.GameDtos.SingleGameQueryDto;
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
            SELECT new com.example.gameStore.dtos.GameDtos.SingleGameQueryDto(
                g.id, g.name, g.quantity, g.thumbnail, g.images, g.developer, g.releaseDate,
                g.systemRequirements, g.price, g.description, g.sku, g.rating, g.isActive,
                g.genreList, g.playerSupport, r.id, r.description, r.starRating, r.createdAt, u.username
            )
            FROM Review r
            JOIN r.gameId g
            JOIN r.userId u
            WHERE g.id = :gameId
            """)
    Optional<List<SingleGameQueryDto>> getSingleGame(@Param("gameId") UUID gameId);
}


