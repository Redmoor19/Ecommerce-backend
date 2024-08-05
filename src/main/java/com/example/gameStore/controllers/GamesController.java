package com.example.gameStore.controllers;


import com.example.gameStore.dtos.GameDto;
import com.example.gameStore.dtos.KeyDto;
import com.example.gameStore.dtos.ReviewDto;
import com.example.gameStore.enums.Genre;
import com.example.gameStore.enums.PlayerSupport;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/")
@AllArgsConstructor
public class GamesController {

    @GetMapping("games")
    public List<GameDto> findAllGames() {
        return List.of(
                new GameDto(UUID.randomUUID(), "Adventure Quest", List.of(Genre.ADVENTURE), 85,
                        "http://example.com/thumb1.jpg", List.of("http://example.com/image1.jpg"),
                        "Quest Devs", new Date(), "4GB RAM, 2GB GPU", List.of(PlayerSupport.SINGLE_PLAYER),
                        19.99f, "An epic adventure game", "SKU12345", true, 4),
                new GameDto(UUID.randomUUID(), "Space Explorer", List.of(Genre.SIMULATION), 90,
                        "http://example.com/thumb2.jpg", List.of("http://example.com/image2.jpg"),
                        "Space Devs", new Date(), "8GB RAM, 4GB GPU", List.of(PlayerSupport.MULTIPLAYER),
                        29.99f, "Explore the universe", "SKU67890", true, 2),
                new GameDto(UUID.randomUUID(), "Mystery Mansion", List.of(Genre.PUZZLE), 75,
                        "http://example.com/thumb3.jpg", List.of("http://example.com/image3.jpg"),
                        "Mystery Devs", new Date(), "6GB RAM, 3GB GPU", List.of(PlayerSupport.SINGLE_PLAYER),
                        14.99f, "A spooky horror game", "SKU54321", false, 3)
        );
    }

    @GetMapping("games/{id}")
    public GameDto getGameById(@PathVariable(required = true, name = "id") UUID id) {
        return new GameDto(UUID.randomUUID(), "Fantasy World", List.of(Genre.RPG), 88,
                "http://example.com/thumb4.jpg", List.of("http://example.com/image4.jpg"),
                "Fantasy Devs", new Date(), "8GB RAM, 4GB GPU", List.of(PlayerSupport.MMO),
                39.99f, "A magical RPG adventure", "SKU98765", true, 4);
    }

    @PostMapping("games")
    public GameDto createGame(@RequestBody GameDto gameDto) {
        //Creation a new Game;
        return new GameDto(UUID.randomUUID(), "Cyber City", List.of(Genre.ACTION), 92,
                "http://example.com/thumb5.jpg", List.of("http://example.com/image5.jpg"),
                "Cyber Devs", new Date(), "16GB RAM, 6GB GPU", List.of(PlayerSupport.ONLINE_COMPETITIVE),
                49.99f, "An action-packed cyber adventure", "SKU11223", true, 5);
    }

    @PatchMapping("games/{id}")
    public GameDto updateGame(@PathVariable UUID id, @RequestBody GameDto gameDto) {
        //Updating an existing game
        return new GameDto(UUID.randomUUID(), "Cyber City", List.of(Genre.ACTION), 92,
                "http://example.com/thumb5.jpg", List.of("http://example.com/image5.jpg"),
                "Cyber Devs", new Date(), "16GB RAM, 6GB GPU", List.of(PlayerSupport.ONLINE_COMPETITIVE),
                49.99f, "An action-packed cyber adventure", "SKU11223", true, 1);
    }

    @DeleteMapping("games/{id}")
    public GameDto deleteGame(@PathVariable UUID id) {
        //Deleting an existing game
        return new GameDto(UUID.randomUUID(), "Cyber City", List.of(Genre.ACTION), 92,
                "http://example.com/thumb5.jpg", List.of("http://example.com/image5.jpg"),
                "Cyber Devs", new Date(), "16GB RAM, 6GB GPU", List.of(PlayerSupport.ONLINE_COMPETITIVE),
                49.99f, "An action-packed cyber adventure", "SKU11223", true, 2);
    }

    @GetMapping("games/genres")
    public List<String> getAllGenres() {
        return Genre.getAllGenres();
    }

    @GetMapping("games/genres/{genre}")
    public List<GameDto> getGamesByGenre(@PathVariable String genre) {
        System.out.println("=====================" + genre + "====================");
        return List.of(new GameDto(
                        UUID.randomUUID(), "Epic Saga", List.of(Genre.RPG, Genre.ACTION), 87,
                        "http://example.com/thumb6.jpg", List.of("http://example.com/image6.jpg"),
                        "Saga Studios", new Date(), "12GB RAM, 4GB GPU", List.of(PlayerSupport.MULTIPLAYER),
                        59.99f, "An epic journey through fantastic worlds", "SKU33445", true, 9
                ),

                new GameDto(
                        UUID.randomUUID(), "Stealth Ops", List.of(Genre.ACTION, Genre.SHOOTER), 94,
                        "http://example.com/thumb7.jpg", List.of("http://example.com/image7.jpg"),
                        "Ops Games", new Date(), "8GB RAM, 3GB GPU", List.of(PlayerSupport.COOPERATIVE),
                        39.99f, "A thrilling stealth and shooting experience", "SKU55667", true, 8
                ));
    }

    @GetMapping("users/me/games")
    public List<GameDto> getCurrentUserGames() {
        return List.of(new GameDto(
                        UUID.randomUUID(), "Epic Saga", List.of(Genre.RPG, Genre.ACTION), 87,
                        "http://example.com/thumb6.jpg", List.of("http://example.com/image6.jpg"),
                        "Saga Studios", new Date(), "12GB RAM, 4GB GPU", List.of(PlayerSupport.MULTIPLAYER),
                        59.99f, "An epic journey through fantastic worlds", "SKU33445", true, 3
                ),

                new GameDto(
                        UUID.randomUUID(), "Stealth Ops", List.of(Genre.ACTION, Genre.SHOOTER), 94,
                        "http://example.com/thumb7.jpg", List.of("http://example.com/image7.jpg"),
                        "Ops Games", new Date(), "8GB RAM, 3GB GPU", List.of(PlayerSupport.COOPERATIVE),
                        39.99f, "A thrilling stealth and shooting experience", "SKU55667", true, 8
                ));
    }

    @GetMapping("users/me/games/favourites")
    public List<GameDto> getCurrentUserFavouriteGames() {
        return List.of(new GameDto(
                        UUID.randomUUID(), "Epic Saga", List.of(Genre.RPG, Genre.ACTION), 87,
                        "http://example.com/thumb6.jpg", List.of("http://example.com/image6.jpg"),
                        "Saga Studios", new Date(), "12GB RAM, 4GB GPU", List.of(PlayerSupport.MULTIPLAYER),
                        59.99f, "An epic journey through fantastic worlds", "SKU33445", true, 4
                ),

                new GameDto(
                        UUID.randomUUID(), "Stealth Ops", List.of(Genre.ACTION, Genre.SHOOTER), 94,
                        "http://example.com/thumb7.jpg", List.of("http://example.com/image7.jpg"),
                        "Ops Games", new Date(), "8GB RAM, 3GB GPU", List.of(PlayerSupport.COOPERATIVE),
                        39.99f, "A thrilling stealth and shooting experience", "SKU55667", true, 8
                ));
    }

    @PostMapping("users/me/games/favourites/{gameId}")
    public GameDto addCurrentUserFavoriteGame(@PathVariable UUID gameId) {
        //Adding an existing game to favorites;
        System.out.println("============================" + gameId + "============================");
        return new GameDto(UUID.randomUUID(), "Cyber City", List.of(Genre.ACTION), 92,
                "http://example.com/thumb5.jpg", List.of("http://example.com/image5.jpg"),
                "Cyber Devs", new Date(), "16GB RAM, 6GB GPU", List.of(PlayerSupport.ONLINE_COMPETITIVE),
                49.99f, "An action-packed cyber adventure", "SKU11223", true, 5);
    }

    @DeleteMapping("users/me/games/favourites/{gameId}")
    public GameDto deleteCurrentUserFavoriteGame(@PathVariable UUID gameId) {
        //Deleting a game from favorites;
        System.out.println("============================" + gameId + "============================");
        return new GameDto(UUID.randomUUID(), "Cyber City", List.of(Genre.ACTION), 92,
                "http://example.com/thumb5.jpg", List.of("http://example.com/image5.jpg"),
                "Cyber Devs", new Date(), "16GB RAM, 6GB GPU", List.of(PlayerSupport.ONLINE_COMPETITIVE),
                49.99f, "An action-packed cyber adventure", "SKU11223", true, 3);
    }

    @GetMapping("games/{gameId}/reviews")
    public List<ReviewDto> getGameReviews(@PathVariable UUID gameId) {
        return List.of(new ReviewDto(
                UUID.randomUUID(),
                UUID.randomUUID(),
                gameId,
                "Great game, highly recommended!",
                5,
                new Date()
        ), new ReviewDto(
                UUID.randomUUID(),
                UUID.randomUUID(),
                gameId,
                "It is okay, but a bit boring.",
                3,
                new Date()
        ), new ReviewDto(
                UUID.randomUUID(),
                UUID.randomUUID(),
                gameId,
                "Not satisfied with the graphic quality. Expected better.",
                2,
                new Date()
        ));
    }

    @PostMapping("games/{gameId}/reviews/{reviewId}")
    public ReviewDto createReview(@PathVariable UUID gameId, @PathVariable UUID reviewId) {
        System.out.println("============================" + gameId + "***" + reviewId + "============================");
        return new ReviewDto(
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID(),
                "Fantastic game! Buy it!",
                5,
                new Date()
        );
    }

    @PatchMapping("games/{gameId}/reviews/{reviewId}")
    public ReviewDto updateReview(@PathVariable UUID gameId, @PathVariable UUID reviewId) {
        System.out.println("============================" + gameId + "***" + reviewId + "============================");
        return new ReviewDto(
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID(),
                "I didn't like it at all! Don't buy it!",
                1,
                new Date()
        );
    }

    @DeleteMapping("games/{gameId}/reviews/{reviewId}")
    public ReviewDto deleteReview(@PathVariable UUID gameId, @PathVariable UUID reviewId) {
        System.out.println("============================" + gameId + "***" + reviewId + "============================");
        return new ReviewDto(
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID(),
                "Hello guy, how is it going? Ohh, wrong app!",
                1,
                new Date()
        );
    }

    @PostMapping("games/{gameId}/keys")
    public KeyDto addKeyToGame(@PathVariable UUID gameId) {
        System.out.println("============================" + gameId + "============================");
        return new KeyDto(
                UUID.randomUUID(),
                UUID.randomUUID(),
                new Date(),
                gameId
        );
    }
}
