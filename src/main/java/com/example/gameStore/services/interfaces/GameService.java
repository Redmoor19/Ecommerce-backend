package com.example.gameStore.services.interfaces;

import com.example.gameStore.dtos.GameDtos.CreateGameRequestDto;
import com.example.gameStore.dtos.GameDtos.UpdateGameRequestDto;
import com.example.gameStore.dtos.KeyDto.KeyCreationDto;
import com.example.gameStore.dtos.GameDtos.GameDto;
import com.example.gameStore.dtos.GameDtos.SingleGameWithReviewsDto;
import com.example.gameStore.dtos.ReviewDtos.ReviewDto;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;

public interface GameService {

    List<GameDto> findAllGames();

    Optional<SingleGameWithReviewsDto> getGameById(String id);

    Optional<GameDto> createGame(CreateGameRequestDto createGameRequestDto);

    Optional<GameDto> updateGame(@RequestBody UpdateGameRequestDto updateGameRequestDto);

    boolean deleteGame(String id);

    List<String> getAllGenres();

    List<GameDto> getGamesByGenre(String genre);

    Optional<ReviewDto> createReview(String gameId, String userId, ReviewDto reviewDto);

    Optional<ReviewDto> updateReview(String gameId, String reviewId);

    boolean deleteReview(String gameId, String reviewId);

    Optional<KeyCreationDto> addKeyToGame(String gameId);

    Optional<Integer> countGameKeys(String gameId);
}
