package com.example.gameStore.controllers;


import com.example.gameStore.dtos.GameDtos.CreateGameRequestDto;
import com.example.gameStore.dtos.GameDtos.GameDto;
import com.example.gameStore.dtos.GameDtos.GamesListResponseDto;
import com.example.gameStore.dtos.GameDtos.SingleGameWithReviewsDto;
import com.example.gameStore.dtos.GameDtos.UpdateGameRequestDto;
import com.example.gameStore.dtos.GlobalResponse;
import com.example.gameStore.dtos.KeyDto.KeyCreationDto;
import com.example.gameStore.dtos.ReviewDtos.CreateOrUpdateReviewRequestDto;
import com.example.gameStore.dtos.ReviewDtos.ReviewDto;
import com.example.gameStore.enums.Genre;
import com.example.gameStore.enums.PlayerSupport;
import com.example.gameStore.services.interfaces.GameService;
import com.example.gameStore.shared.exceptions.ResourceNotFoundException;
import com.example.gameStore.utilities.TypeConverter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/")
@AllArgsConstructor
public class GameController {

    @Autowired
    private final GameService gameService;

    @GetMapping("games/all")
    public ResponseEntity<GlobalResponse<GamesListResponseDto>> findAllGames(
            @RequestParam(value = "sort", defaultValue = "name") String sortField,
            @RequestParam(value = "order", defaultValue = "asc") String sortOrder,
            @RequestParam(value = "page", defaultValue = "1") String pageNumber,
            @RequestParam(value = "size", defaultValue = "20") String pageSize,
            @RequestParam(value = "search", required = false) String searchKeyword,
            @RequestParam(value = "genres", required = false) List<String> genres,
            @RequestParam(value = "playerSupports", required = false) List<String> playerSupport
    ) {

        int convertedPageNumber = TypeConverter.convertStringToInt(pageNumber, "Wrong data format for page number");
        int convertedPageSize = TypeConverter.convertStringToInt(pageSize, "Wrong data format for page size");

        GamesListResponseDto games = gameService.findAllGames(
                sortField,
                sortOrder,
                convertedPageNumber,
                convertedPageSize,
                searchKeyword,
                genres,
                playerSupport
        );
        return ResponseEntity.ok(new GlobalResponse<>(games));
    }

    @GetMapping("games/active")
    public ResponseEntity<GlobalResponse<GamesListResponseDto>> findAllActiveGames(
            @RequestParam(value = "sort", defaultValue = "name") String sortField,
            @RequestParam(value = "order", defaultValue = "asc") String sortOrder,
            @RequestParam(value = "page", defaultValue = "1") String pageNumber,
            @RequestParam(value = "size", defaultValue = "20") String pageSize,
            @RequestParam(value = "search", required = false) String searchKeyword,
            @RequestParam(value = "genres", required = false) List<String> genres,
            @RequestParam(value = "playerSupports", required = false) List<String> playerSupport) {

        int convertedPageNumber = TypeConverter.convertStringToInt(pageNumber, "Wrong data format for page number");
        int convertedPageSize = TypeConverter.convertStringToInt(pageSize, "Wrong data format for page size");

        GamesListResponseDto games = gameService.findAllActiveGames(
                sortField,
                sortOrder,
                convertedPageNumber,
                convertedPageSize,
                searchKeyword,
                genres,
                playerSupport);
        return ResponseEntity.ok(new GlobalResponse<>(games));
    }

    @GetMapping("games/{id}")
    public ResponseEntity<GlobalResponse<SingleGameWithReviewsDto>> getGameById(@PathVariable String id) {
        Optional<SingleGameWithReviewsDto> gameWithReviews = gameService.getGameById(id);
        return gameWithReviews
                .map(game -> ResponseEntity.ok(new GlobalResponse<>(game)))
                .orElseThrow(() -> new ResourceNotFoundException("Game with such Id not found"));
    }

    @PostMapping("games")
    public ResponseEntity<GlobalResponse<GameDto>> createGame(@RequestBody CreateGameRequestDto createGameRequestDto) {
        Optional<GameDto> game = gameService.createGame(createGameRequestDto);
        return game
                .map(createdGame -> ResponseEntity.ok(new GlobalResponse<>(createdGame)))
                .orElseThrow(() -> new RuntimeException("Failed to create game"));
    }

    @PatchMapping("games")
    public ResponseEntity<GlobalResponse<GameDto>> updateGame(@RequestBody UpdateGameRequestDto updateGameRequestDto) {
        Optional<GameDto> game = gameService.updateGame(updateGameRequestDto);
        return game
                .map(updatedGame -> ResponseEntity.ok(new GlobalResponse<>(updatedGame)))
                .orElseThrow(() -> new RuntimeException("Failed to update game"));
    }

    @PatchMapping("games/deactivation/{gameId}")
    public ResponseEntity<GlobalResponse<GameDto>> deactivateGame(@PathVariable String gameId) {
        Optional<GameDto> game = gameService.deactivateGame(gameId);
        return game
                .map(deactivatedGame -> ResponseEntity.ok(new GlobalResponse<>(deactivatedGame)))
                .orElseThrow(() -> new ResourceNotFoundException("Game with such Id not found"));
    }

    @PatchMapping("games/activation/{gameId}")
    public ResponseEntity<GlobalResponse<GameDto>> activateGame(@PathVariable String gameId) {
        Optional<GameDto> game = gameService.activateGame(gameId);
        return game
                .map(activatedGame -> ResponseEntity.ok(new GlobalResponse<>(activatedGame)))
                .orElseThrow(() -> new ResourceNotFoundException("Game with such Id not found"));
    }

    @GetMapping("games/genres")
    public ResponseEntity<GlobalResponse<List<String>>> getAllGenres() {
        List<String> genreList = Genre.getAllGenresString();
        return ResponseEntity.ok(new GlobalResponse<>(genreList));
    }

    @GetMapping("games/genres/{genre}")
    public ResponseEntity<GlobalResponse<List<GameDto>>> getGamesByGenre(@PathVariable String genre) {
        List<GameDto> gamesByGenre = gameService.getGamesByGenre(genre);
        return ResponseEntity.ok(new GlobalResponse<>(gamesByGenre));
    }

    @GetMapping("games/player-support")
    public ResponseEntity<GlobalResponse<List<String>>> getAllPlayerSupport() {
        List<String> playerSupport = PlayerSupport.getAllPlayerSupportString();
        return ResponseEntity.ok(new GlobalResponse<>(playerSupport));
    }

    @GetMapping("games/player-support/{playerSupport}")
    public ResponseEntity<GlobalResponse<List<GameDto>>> getGamesByPlayerSupport(@PathVariable String playerSupport) {
        List<GameDto> gamesByPlayerSupport = gameService.getGamesByPlayerSupport(playerSupport);
        return ResponseEntity.ok(new GlobalResponse<>(gamesByPlayerSupport));
    }

    @PostMapping("games/reviews/{gameId}")
    public ResponseEntity<GlobalResponse<ReviewDto>> createReview(HttpServletRequest request, @PathVariable String gameId, @RequestBody CreateOrUpdateReviewRequestDto reviewDto) {
        String userId = (String) request.getAttribute("userId");
        Optional<ReviewDto> review = gameService.createReview(gameId, userId, reviewDto);
        return review
                .map(createdReview -> ResponseEntity.ok(new GlobalResponse<>(createdReview)))
                .orElseThrow(() -> new RuntimeException("Failed to create review"));
    }

    @PatchMapping("games/reviews/{reviewId}")
    public ResponseEntity<GlobalResponse<ReviewDto>> updateReview(HttpServletRequest request, @PathVariable String reviewId, @RequestBody CreateOrUpdateReviewRequestDto createOrUpdateReviewRequestDto) {
        String userId = (String) request.getAttribute("userId");
        Optional<ReviewDto> review = gameService.updateReview(reviewId, userId, createOrUpdateReviewRequestDto);
        return review
                .map(updatedReview -> ResponseEntity.ok(new GlobalResponse<>(updatedReview)))
                .orElseThrow(() -> new ResourceNotFoundException("Review with such Id not found or you do not have permission to update it"));
    }

    @DeleteMapping("games/reviews/{reviewId}")
    public ResponseEntity<GlobalResponse<Void>> deleteReview(HttpServletRequest request, @PathVariable String reviewId) {
        String userId = (String) request.getAttribute("userId");
        if (gameService.deleteReview(reviewId, userId)) {
            return ResponseEntity.ok(new GlobalResponse<>(null));
        }
        throw new ResourceNotFoundException("Review with such Id not found or you do not have permission to delete it");
    }

    @PostMapping("games/keys/{gameId}")
    public ResponseEntity<GlobalResponse<KeyCreationDto>> addKeyToGame(@PathVariable String gameId) {
        Optional<KeyCreationDto> key = gameService.addKeyToGame(gameId);
        return key
                .map(createdKey -> ResponseEntity.ok(new GlobalResponse<>(createdKey)))
                .orElseThrow(() -> new ResourceNotFoundException("Failed to add key to game"));
    }

    @GetMapping("games/keys/{gameId}")
    public ResponseEntity<GlobalResponse<Integer>> getGameKeysAmount(@PathVariable String gameId) {
        Optional<Integer> keysAmountOpt = gameService.countGameKeys(gameId);
        return keysAmountOpt
                .map(keysAmount -> ResponseEntity.ok(new GlobalResponse<>(keysAmount)))
                .orElseThrow(() -> new ResourceNotFoundException("Game with such Id not found"));
    }

}
