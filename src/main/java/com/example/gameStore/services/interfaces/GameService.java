package com.example.gameStore.services.interfaces;

import com.example.gameStore.dtos.GameDtos.CreateGameRequestDto;
import com.example.gameStore.dtos.GameDtos.GameDto;
import com.example.gameStore.dtos.GameDtos.GamesListResponseDto;
import com.example.gameStore.dtos.GameDtos.SingleGameWithReviewsDto;
import com.example.gameStore.dtos.GameDtos.UpdateGameRequestDto;
import com.example.gameStore.dtos.KeyDto.KeyCreationDto;
import com.example.gameStore.dtos.ReviewDtos.CreateOrUpdateReviewRequestDto;
import com.example.gameStore.dtos.ReviewDtos.ReviewDto;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;

public interface GameService {

    GamesListResponseDto findAllGames(String sortField, String sortOrder, int pageNumber, int pageSize, String searchKeyword, List<String> genres, List<String> playerSupport);

    GamesListResponseDto findAllActiveGames(String sortField, String sortOrder, int pageNumber, int pageSize, String searchKeyword, List<String> genres, List<String> playerSupport);

    Optional<SingleGameWithReviewsDto> getGameById(String id);

    Optional<GameDto> createGame(CreateGameRequestDto createGameRequestDto);

    Optional<GameDto> updateGame(@RequestBody UpdateGameRequestDto updateGameRequestDto);

    Optional<GameDto> deactivateGame(String gameId);

    Optional<GameDto> activateGame(String gameId);

    List<GameDto> getGamesByGenre(String genre);

    List<GameDto> getGamesByPlayerSupport(String playerSupport);

    Optional<ReviewDto> createReview(String gameId, String userId, CreateOrUpdateReviewRequestDto reviewDto);

    Optional<ReviewDto> updateReview(String reviewId, String userId, CreateOrUpdateReviewRequestDto createOrUpdateReviewRequestDto);

    boolean deleteReview(String reviewId, String userId);

    Optional<KeyCreationDto> addKeyToGame(String gameId);

    Optional<Integer> countGameKeys(String gameId);
}
