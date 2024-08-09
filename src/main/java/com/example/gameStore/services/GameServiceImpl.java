package com.example.gameStore.services;

import com.example.gameStore.dtos.GameDtos.GameDto;
import com.example.gameStore.dtos.GameDtos.SingleGameDto;
import com.example.gameStore.dtos.KeyDto;
import com.example.gameStore.dtos.ReviewDtos.ReviewDto;
import com.example.gameStore.entities.Game;
import com.example.gameStore.entities.Review;
import com.example.gameStore.enums.Genre;
import com.example.gameStore.enums.PlayerSupport;
import com.example.gameStore.repositories.GameRepository;
import com.example.gameStore.repositories.ReviewRepository;
import com.example.gameStore.services.interfaces.GameService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class GameServiceImpl implements GameService {

    private final ModelMapper modelMapper = new ModelMapper();
    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private ReviewRepository reviewRepository;

    @Override
    public List<GameDto> findAllGames() {
        List<Game> games = gameRepository.findAll();
        return games.stream().map(game -> modelMapper.map(game, GameDto.class)).toList();
    }

    @Override
    public Optional<SingleGameDto> getGameById(String id) {
        Optional<Game> foundGame = gameRepository.findById(UUID.fromString(id));
        return foundGame.map(game -> modelMapper.map(game, SingleGameDto.class));
    }

    @Override
    public Optional<GameDto> createGame(GameDto gameDto) {
        Game createGame = modelMapper.map(gameDto, Game.class);
        System.out.println("Creating user is:\n" + createGame.toString());
        gameRepository.save(createGame);
        return Optional.of(modelMapper.map(createGame, GameDto.class));

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
        return List.of();
    }

    @Override
    public Optional<ReviewDto> createReview(String gameId, String userId, ReviewDto reviewDto) {
        Review review = modelMapper.map(reviewDto, Review.class);
        review.setUserId(UUID.fromString(userId));
        review.setGameId(UUID.fromString(gameId));
        Review savedReview = reviewRepository.save(review);
        return Optional.of(modelMapper.map(savedReview, ReviewDto.class));
    }

    @Override
    public Optional<ReviewDto> updateReview(String gameId, String reviewId) {
        System.out.println("============================" + gameId + "***" + reviewId + "============================");
        return Optional.empty();
    }

    @Override
    public boolean deleteReview(String gameId, String reviewId) {
        System.out.println("============================" + gameId + "***" + reviewId + "============================");
        return true;
    }

    @Override
    public Optional<ReviewDto> getReviewById(String gameId, String reviewId) {
        return Optional.empty();
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
