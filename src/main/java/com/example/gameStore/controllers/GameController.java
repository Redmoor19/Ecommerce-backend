package com.example.gameStore.controllers;


import com.example.gameStore.dtos.GameDtos.CreateGameRequestDto;
import com.example.gameStore.dtos.GameDtos.GameDto;
import com.example.gameStore.dtos.GameDtos.SingleGameWithReviewsDto;
import com.example.gameStore.dtos.GameDtos.UpdateGameRequestDto;
import com.example.gameStore.dtos.KeyDto.KeyCreationDto;
import com.example.gameStore.dtos.ReviewDtos.CreateOrUpdateReviewRequestDto;
import com.example.gameStore.dtos.ReviewDtos.ReviewDto;
import com.example.gameStore.enums.Genre;
import com.example.gameStore.enums.PlayerSupport;
import com.example.gameStore.services.interfaces.GameService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/")
@AllArgsConstructor
public class GameController {

    @Autowired
    private final GameService gameService;

    @GetMapping("games")
    public ResponseEntity<List<GameDto>> findAllGames() {
        List<GameDto> games = gameService.findAllGames();
        if (games.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(games);
    }

    @GetMapping("games/{id}")
    public ResponseEntity<SingleGameWithReviewsDto> getGameById(@PathVariable(required = true, name = "id") String id) {
        Optional<SingleGameWithReviewsDto> gameWithReviews = gameService.getGameById(id);
        return gameWithReviews.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("games")
    public ResponseEntity<GameDto> createGame(@RequestBody CreateGameRequestDto createGameRequestDto) {
        Optional<GameDto> game = gameService.createGame(createGameRequestDto);
        return game.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PatchMapping("games")
    public ResponseEntity<GameDto> updateGame(@RequestBody UpdateGameRequestDto updateGameRequestDto) {
        Optional<GameDto> game = gameService.updateGame(updateGameRequestDto);
        return game.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PatchMapping("games/deactivation/{gameId}")
    public ResponseEntity<GameDto> deactivateGame(@PathVariable String gameId) {
        Optional<GameDto> game = gameService.deactivateGame(gameId);
        return game.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PatchMapping("games/activation/{gameId}")
    public ResponseEntity<GameDto> activateGame(@PathVariable String gameId) {
        Optional<GameDto> game = gameService.activateGame(gameId);
        return game.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("games/genres")
    public ResponseEntity<List<String>> getAllGenres() {
        List<String> genreList = Genre.getAllGenres();
        if (genreList.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(genreList);
    }

    @GetMapping("games/genres/{genre}")
    public ResponseEntity<List<GameDto>> getGamesByGenre(@PathVariable String genre) {
        List<GameDto> gamesByGenre = gameService.getGamesByGenre(genre);
        if (gamesByGenre.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(gamesByGenre);
    }

    @GetMapping("games/player-support")
    public ResponseEntity<List<String>> getAllPlayerSupport() {
        List<String> playerSupport = PlayerSupport.getAllPlayerSupport();
        if (playerSupport.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(playerSupport);
    }

    @GetMapping("games/player-support/{playerSupport}")
    public ResponseEntity<List<GameDto>> getGamesByPlayerSupport(@PathVariable String playerSupport) {
        List<GameDto> gamesByPlayerSupport = gameService.getGamesByPlayerSupport(playerSupport);
        if (gamesByPlayerSupport.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(gamesByPlayerSupport);
    }


    // I pass userId through URL temporarily since I can't get ID from request. Remember to delete after auth is complete!
    @PostMapping("games/{gameId}/reviews/{userId}")
    public ResponseEntity<ReviewDto> createReview(@PathVariable String gameId, @PathVariable String userId, @RequestBody CreateOrUpdateReviewRequestDto reviewDto) {
        Optional<ReviewDto> review = gameService.createReview(gameId, userId, reviewDto);
        return review.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PatchMapping("games/reviews/{reviewId}/user/{userId}")
    public ResponseEntity<ReviewDto> updateReview(@PathVariable String reviewId, @PathVariable String userId, @RequestBody CreateOrUpdateReviewRequestDto createOrUpdateReviewRequestDto) {
        Optional<ReviewDto> review = gameService.updateReview(reviewId, userId, createOrUpdateReviewRequestDto);
        return review.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("games/reviews/{reviewId}/user/{userId}")
    public ResponseEntity<ReviewDto> deleteReview(@PathVariable String reviewId, @PathVariable String userId) {
        if (gameService.deleteReview(reviewId, userId)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("games/{gameId}/keys")
    public ResponseEntity<KeyCreationDto> addKeyToGame(@PathVariable String gameId) {
        Optional<KeyCreationDto> key = gameService.addKeyToGame(gameId);
        return key.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("games/keys/{gameId}")
    public ResponseEntity<Integer> getGameKeysAmount(@PathVariable String gameId) {
        Optional<Integer> keysAmountOpt = gameService.countGameKeys(gameId);
        return keysAmountOpt
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

}
