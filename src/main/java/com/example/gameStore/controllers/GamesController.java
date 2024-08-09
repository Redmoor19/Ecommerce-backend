package com.example.gameStore.controllers;


import com.example.gameStore.dtos.GameDtos.GameDto;
import com.example.gameStore.dtos.GameDtos.SingleGameDto;
import com.example.gameStore.dtos.KeyDto;
import com.example.gameStore.dtos.ReviewDtos.ReviewDto;
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
    public ResponseEntity<SingleGameDto> getGameById(@PathVariable(required = true, name = "id") String id) {
        Optional<SingleGameDto> game = gameService.getGameById(id);
        return game.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
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
        List<String> genreList = gameService.getAllGenres();
        if (genreList.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(genreList);
    }

    @GetMapping("games/genres/{genre}")
    public ResponseEntity<List<GameDto>> getGamesByGenre(@PathVariable String genre) {
        List<GameDto> games = gameService.getGamesByGenre(genre);
        if (games.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(games);
    }

    @GetMapping("users/me/games")
    public ResponseEntity<List<GameDto>> getCurrentUserGames() {
        List<GameDto> games = gameService.getCurrentUserGames();
        if (games.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(games);
    }

    @GetMapping("users/me/games/favourites")
    public ResponseEntity<List<GameDto>> getCurrentUserFavouriteGames() {
        List<GameDto> games = gameService.getCurrentUserFavouriteGames();
        if (games.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(games);
    }

    @PostMapping("users/me/games/favourites/{gameId}")
    public ResponseEntity<GameDto> addCurrentUserFavoriteGame(@PathVariable String gameId) {
        Optional<GameDto> game = gameService.addCurrentUserFavoriteGame(gameId);
        return game.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("users/me/games/favourites/{gameId}")
    public ResponseEntity<GameDto> deleteCurrentUserFavoriteGame(@PathVariable String gameId) {
        if (gameService.deleteFavoriteGameOfCurrentUser(gameId)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
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

    @PostMapping("games/{gameId}/keys")
    public ResponseEntity<KeyDto> addKeyToGame(@PathVariable String gameId) {
        Optional<KeyDto> key = gameService.addKeyToGame(gameId);
        return key.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("games/{gameId}/keys")
    public ResponseEntity<Integer> getGameKeysAmount(@PathVariable String gameId) {
        Optional<Integer> keysAmount = gameService.countGameKeys(gameId);
        return keysAmount.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

}
