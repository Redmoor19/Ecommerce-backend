package com.example.gameStore.services.interfaces;

import com.example.gameStore.dtos.GameDto;
import com.example.gameStore.dtos.KeyDto;
import com.example.gameStore.dtos.ReviewDto;

import java.util.List;
import java.util.Optional;

public interface GameService {

    public List<GameDto> findAllGames();

    public Optional<GameDto> getGameById(String id);

    public Optional<GameDto> createGame(GameDto gameDto);

    public Optional<GameDto> updateGame(String id, GameDto gameDto);

    public boolean deleteGame(String id);

    public List<String> getAllGenres();

    public List<GameDto> getGamesByGenre(String genre);

    public List<GameDto> getCurrentUserGames();

    public List<GameDto> getCurrentUserFavouriteGames();

    public Optional<GameDto> addCurrentUserFavoriteGame(String gameId);

    public boolean deleteCurrentUserFavoriteGame(String gameId);

    public List<ReviewDto> getGameReviews(String gameId);

    public Optional<ReviewDto> createReview(String gameId);

    public Optional<ReviewDto> updateReview(String gameId, String reviewId);

    public boolean deleteReview(String gameId, String reviewId);

    public Optional<ReviewDto> getReviewById(String gameId, String reviewId);

    public Optional<KeyDto> addKeyToGame(String gameId);

    public Optional<Integer> countGameKeys(String gameId);
}
