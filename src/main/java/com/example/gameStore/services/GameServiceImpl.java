package com.example.gameStore.services;

import com.example.gameStore.dtos.GameDtos.GameDto;
import com.example.gameStore.dtos.GameDtos.SingleGameWithReviewsDto;
import com.example.gameStore.dtos.KeyCreationDto;
import com.example.gameStore.dtos.ReviewDtos.EmbeddedReviewDto;
import com.example.gameStore.dtos.ReviewDtos.ReviewDto;
import com.example.gameStore.entities.Game;
import com.example.gameStore.entities.Key;
import com.example.gameStore.entities.Review;
import com.example.gameStore.entities.User;
import com.example.gameStore.enums.Genre;
import com.example.gameStore.enums.PlayerSupport;
import com.example.gameStore.repositories.GameRepository;
import com.example.gameStore.repositories.KeyRepository;
import com.example.gameStore.repositories.ReviewRepository;
import com.example.gameStore.repositories.UserRepository;
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
    private KeyRepository keyRepository;
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public List<GameDto> findAllGames() {
        List<Game> games = gameRepository.findAll();
        return games.stream().map(game -> modelMapper.map(game, GameDto.class)).toList();
    }

    @Override
    public Optional<SingleGameWithReviewsDto> getGameById(String id) {
        Optional<Game> game = gameRepository.findById(UUID.fromString(id));
        if (game.isEmpty()) return Optional.empty();
        List<EmbeddedReviewDto> reviews = reviewRepository.findReviewsByGameId(UUID.fromString(id));
        SingleGameWithReviewsDto singleGameWithReviewsDto = modelMapper.map(game, SingleGameWithReviewsDto.class);
        singleGameWithReviewsDto.setReviews(reviews);
        return Optional.of(singleGameWithReviewsDto);
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
    public Optional<List<String>> getAllGenres() {
        return gameRepository.getAllGenresList();
    }

    @Override
    public Optional<List<Game>> getGamesByGenre(String genre) {
        return gameRepository.getGamesByGenre(genre);
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
    public Optional<ReviewDto> createReview(String gameId, String userId, ReviewDto reviewDto) {
        Review review = modelMapper.map(reviewDto, Review.class);
        Optional<User> optUser = userRepository.findById(UUID.fromString(userId));
        Optional<Game> optGame = gameRepository.findById(UUID.fromString(gameId));
        if (optUser.isEmpty() || optGame.isEmpty()) return Optional.empty();
        review.setUserId(optUser.get());
        review.setGameId(optGame.get());
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
    public Optional<KeyCreationDto> addKeyToGame(KeyCreationDto keyCreationDto) {
        Game game = gameRepository.findById(keyCreationDto.getGameId())
                .orElseThrow(() -> new RuntimeException("Game not found with ID: " + keyCreationDto.getGameId()));
        Key key = new Key();
        key.setValue(keyCreationDto.getValue());
        key.setGame(game);
        keyRepository.save(key);
        return Optional.of(modelMapper.map(key, KeyCreationDto.class));
    }

    @Override
    public Optional<Integer> countGameKeys(String gameId) {
        UUID convertedGameId = UUID.fromString(gameId);
        return gameRepository.getGameKeysAmount(convertedGameId);
    }
}
