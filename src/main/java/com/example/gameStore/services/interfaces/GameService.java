package com.example.gameStore.services.interfaces;

import com.example.gameStore.dtos.GameDtos.CreateGameDto;
import com.example.gameStore.dtos.KeyCreationDto;
import com.example.gameStore.entities.Game;
import com.example.gameStore.dtos.GameDtos.GameDto;
import com.example.gameStore.dtos.GameDtos.SingleGameWithReviewsDto;
import com.example.gameStore.dtos.ReviewDtos.ReviewDto;

import java.util.List;
import java.util.Optional;

public interface GameService {

    List<GameDto> findAllGames();

    Optional<SingleGameWithReviewsDto> getGameById(String id);

    Optional<GameDto> createGame(CreateGameDto createGameDto);

    Optional<GameDto> updateGame(String id, GameDto gameDto);

    boolean deleteGame(String id);

    Optional<List<String>> getAllGenres();

    Optional<List<Game>> getGamesByGenre(String genre);

    List<GameDto> getCurrentUserGames();

    List<GameDto> getCurrentUserFavouriteGames();

    Optional<GameDto> addCurrentUserFavoriteGame(String gameId);

    boolean deleteFavoriteGameOfCurrentUser(String gameId);

    Optional<ReviewDto> createReview(String gameId, String userId, ReviewDto reviewDto);

    Optional<ReviewDto> updateReview(String gameId, String reviewId);

    boolean deleteReview(String gameId, String reviewId);

    Optional<ReviewDto> getReviewById(String gameId, String reviewId);

    Optional<KeyCreationDto> addKeyToGame(KeyCreationDto keyCreationDto);

    Optional<Integer> countGameKeys(String gameId);
}
