package com.example.gameStore.controllers;


import com.example.gameStore.dtos.GameDtos.GameDto;
import com.example.gameStore.dtos.GameDtos.SingleGameWithReviewsDto;
import com.example.gameStore.dtos.ReviewDtos.ReviewDto;
import com.example.gameStore.dtos.KeyCreationDto;
import com.example.gameStore.entities.Game;
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
public class GamesController {

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
    public ResponseEntity<GameDto> createGame(@RequestBody GameDto gameDto) {
        Optional<GameDto> game = gameService.createGame(gameDto);
        return game.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PatchMapping("games/{id}")
    public ResponseEntity<GameDto> updateGame(@PathVariable String id, @RequestBody GameDto gameDto) {
        Optional<GameDto> game = gameService.updateGame(id, gameDto);
        return game.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("games/{id}")
    public ResponseEntity<Void> deleteGame(@PathVariable String id) {
        if (gameService.deleteGame(id)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("games/genres")
    public ResponseEntity<List<String>> getAllGenres() {
        Optional<List<String>> genreList = gameService.getAllGenres();
        return genreList.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.noContent().build());
    }

    @GetMapping("games/genres/{genre}")
    public ResponseEntity<List<Game>> getGamesByGenre(@PathVariable String genre) {
        Optional<List<Game>> gamesByGenre = gameService.getGamesByGenre(genre);
        return gamesByGenre.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.noContent().build());
    }

    @GetMapping("users/me/games")
    public ResponseEntity<List<GameDto>> getCurrentUserGames() {
        List<GameDto> games = gameService.getCurrentUserGames();
        if (games.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(games);
    }

    @GetMapping("games/{gameId}/reviews")
    public ResponseEntity<List<ReviewDto>> getGameReviews(@PathVariable String gameId) {
        List<ReviewDto> reviews = gameService.getGameReviews(gameId);
        if (reviews.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(reviews);
    }

    // I pass userId through URL temporarily since I can't get ID from request. Remember to delete after auth is complete!
    @PostMapping("games/{gameId}/reviews/{userId}")
    public ResponseEntity<ReviewDto> createReview(@PathVariable String gameId, @PathVariable String userId, @RequestBody ReviewDto reviewDto) {
        Optional<ReviewDto> review = gameService.createReview(gameId, userId, reviewDto);
        return review.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PatchMapping("games/{gameId}/reviews/{reviewId}")
    public ResponseEntity<ReviewDto> updateReview(@PathVariable String gameId, @PathVariable String reviewId) {
        Optional<ReviewDto> review = gameService.updateReview(gameId, reviewId);
        return review.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("games/{gameId}/reviews/{reviewId}")
    public ResponseEntity<ReviewDto> deleteReview(@PathVariable String gameId, @PathVariable String reviewId) {
        if (gameService.deleteReview(gameId, reviewId)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("games/{gameId}/reviews/{reviewId}")
    public ResponseEntity<ReviewDto> getReviewById(@PathVariable String gameId, @PathVariable String reviewId) {
        Optional<ReviewDto> game = gameService.getReviewById(gameId, reviewId);
        return game.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("games/keys")
    public ResponseEntity<KeyCreationDto> addKeyToGame(@RequestBody KeyCreationDto keyCreationDto) {
        Optional<KeyCreationDto> key = gameService.addKeyToGame(keyCreationDto);
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
