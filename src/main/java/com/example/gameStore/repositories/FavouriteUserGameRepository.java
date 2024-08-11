package com.example.gameStore.repositories;

import com.example.gameStore.entities.FavouriteUserGame;
import com.example.gameStore.entities.Game;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface FavouriteUserGameRepository extends JpaRepository<FavouriteUserGame, UUID> {
    @Query("SELECT g FROM Game g JOIN FavouriteUserGame f ON f.game.id = g.id WHERE f.user.id = :userId")
    List<Game> getUserFavourites(@Param("userId") UUID userId);


    @Modifying
    @Transactional
    @Query("DELETE FROM FavouriteUserGame f WHERE f.user.id = :userId AND f.game.id = :gameId")
    int deleteUserFavourite(@Param("userId") UUID userId, @Param("gameId") UUID gameId);
}
