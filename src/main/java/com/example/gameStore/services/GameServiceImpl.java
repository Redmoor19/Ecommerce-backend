package com.example.gameStore.services;

import com.example.gameStore.dtos.GameDto;
import com.example.gameStore.dtos.KeyDto;
import com.example.gameStore.dtos.ReviewDto;
import com.example.gameStore.enums.Genre;
import com.example.gameStore.enums.PlayerSupport;
import com.example.gameStore.services.interfaces.GameService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class GameServiceImpl implements GameService {

    @Override
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

    @Override
    public Optional<GameDto> getGameById(String id) {
        return Optional.of(new GameDto(UUID.fromString(id), "Fantasy World", List.of(Genre.RPG), 88,
                "http://example.com/thumb4.jpg", List.of("http://example.com/image4.jpg"),
                "Fantasy Devs", new Date(), "8GB RAM, 4GB GPU", List.of(PlayerSupport.MMO),
                39.99f, "A magical RPG adventure", "SKU98765", true, 4));
    }

    @Override
    public Optional<GameDto> createGame(GameDto gameDto) {
        return Optional.of(new GameDto(UUID.randomUUID(), "Cyber City", List.of(Genre.ACTION), 92,
                "http://example.com/thumb5.jpg", List.of("http://example.com/image5.jpg"),
                "Cyber Devs", new Date(), "16GB RAM, 6GB GPU", List.of(PlayerSupport.ONLINE_COMPETITIVE),
                49.99f, "An action-packed cyber adventure", "SKU11223", true, 5));

    }

    @Override
    public Optional<GameDto> updateGame(String id, GameDto gameDto) {
        return Optional.of(new GameDto(UUID.fromString(id), "Cyber City", List.of(Genre.ACTION), 92,
                "http://example.com/thumb5.jpg", List.of("http://example.com/image5.jpg"),
                "Cyber Devs", new Date(), "16GB RAM, 6GB GPU", List.of(PlayerSupport.ONLINE_COMPETITIVE),
                49.99f, "An action-packed cyber adventure", "SKU11223", true, 1));
    }

    @Override
    public boolean deleteGame(String id) {
        return true;
    }

    @Override
    public List<String> getAllGenres() {
        return Genre.getAllGenres();
    }

    @Override
    public List<GameDto> getGamesByGenre(String genre) {
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

    @Override
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

    @Override
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

    @Override
    public Optional<GameDto> addCurrentUserFavoriteGame(String gameId) {
        System.out.println("============================" + gameId + "============================");
        return Optional.of(new GameDto(UUID.randomUUID(), "Cyber City", List.of(Genre.ACTION), 92,
                "http://example.com/thumb5.jpg", List.of("http://example.com/image5.jpg"),
                "Cyber Devs", new Date(), "16GB RAM, 6GB GPU", List.of(PlayerSupport.ONLINE_COMPETITIVE),
                49.99f, "An action-packed cyber adventure", "SKU11223", true, 5));
    }

    @Override
    public boolean deleteFavoriteGameOfCurrentUser(String gameId) {
        System.out.println("============================" + gameId + "============================");
        return true;
    }

    @Override
    public List<ReviewDto> getGameReviews(String gameId) {
        return List.of(new ReviewDto(
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.fromString(gameId),
                "Great game, highly recommended!",
                5,
                new Date()
        ), new ReviewDto(
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.fromString(gameId),
                "It is okay, but a bit boring.",
                3,
                new Date()
        ), new ReviewDto(
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.fromString(gameId),
                "Not satisfied with the graphic quality. Expected better.",
                2,
                new Date()
        ));
    }

    @Override
    public Optional<ReviewDto> createReview(String gameId) {
        System.out.println("============================" + gameId + "============================");
        return Optional.of(new ReviewDto(
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID(),
                "Fantastic game! Buy it!",
                5,
                new Date()
        ));
    }

    @Override
    public Optional<ReviewDto> updateReview(String gameId, String reviewId) {
        System.out.println("============================" + gameId + "***" + reviewId + "============================");
        return Optional.of(new ReviewDto(
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID(),
                "I didn't like it at all! Don't buy it!",
                1,
                new Date()
        ));
    }

    @Override
    public boolean deleteReview(String gameId, String reviewId) {
        System.out.println("============================" + gameId + "***" + reviewId + "============================");
        return true;
    }

    @Override
    public Optional<ReviewDto> getReviewById(String gameId, String reviewId) {
        return Optional.of(new ReviewDto(
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID(),
                "I found the review to response it!",
                1,
                new Date()
        ));
    }

    @Override
    public Optional<KeyDto> addKeyToGame(String gameId) {
        System.out.println("============================" + gameId + "============================");
        return Optional.of(new KeyDto(
                UUID.randomUUID(),
                UUID.randomUUID(),
                new Date(),
                UUID.fromString(gameId)
        ));
    }

    @Override
    public Optional<Integer> countGameKeys(String gameId) {
        return Optional.of(6);
    }
}
