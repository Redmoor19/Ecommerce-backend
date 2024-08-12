package com.example.gameStore.services;

import com.example.gameStore.dtos.GameDtos.CreateGameRequestDto;
import com.example.gameStore.dtos.GameDtos.GameDto;
import com.example.gameStore.dtos.GameDtos.SingleGameWithReviewsDto;
import com.example.gameStore.dtos.GameDtos.UpdateGameRequestDto;
import com.example.gameStore.dtos.KeyDto.KeyCreationDto;
import com.example.gameStore.dtos.ReviewDtos.CreateOrUpdateReviewRequestDto;
import com.example.gameStore.dtos.ReviewDtos.EmbeddedReviewDto;
import com.example.gameStore.dtos.ReviewDtos.ReviewDto;
import com.example.gameStore.entities.Game;
import com.example.gameStore.entities.Key;
import com.example.gameStore.entities.Review;
import com.example.gameStore.entities.User;
import com.example.gameStore.repositories.GameRepository;
import com.example.gameStore.repositories.KeyRepository;
import com.example.gameStore.repositories.ReviewRepository;
import com.example.gameStore.repositories.UserRepository;
import com.example.gameStore.services.interfaces.GameService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.NoSuchElementException;
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

    private static <E extends Enum<E>> boolean areNotEnumListsEquals(List<E> list1, List<E> list2) {
        if (list1.size() != list2.size()) {
            return true;
        }

        for (int i = 0; i < list1.size(); i++) {
            if (!list1.get(i).equals(list2.get(i))) {
                return true;
            }
        }
        return false;
    }

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
    public Optional<GameDto> createGame(CreateGameRequestDto createGameRequestDto) {
        Game createGame = modelMapper.map(createGameRequestDto, Game.class);
        gameRepository.save(createGame);
        return Optional.of(modelMapper.map(createGame, GameDto.class));
    }

    @Override
    public Optional<GameDto> updateGame(@RequestBody UpdateGameRequestDto updateGameRequestDto) {
        Optional<Game> updateGame = gameRepository.findById(updateGameRequestDto.getId());
        if (updateGame.isEmpty()) return Optional.empty();
        Game existingGame = updateGame.get();
        if (areNotEnumListsEquals(existingGame.getGenreList(), updateGameRequestDto.getGenreList())) {
            existingGame.setGenreList(updateGameRequestDto.getGenreList());
        }
        if (areNotEnumListsEquals(existingGame.getPlayerSupport(), updateGameRequestDto.getPlayerSupport())) {
            existingGame.setPlayerSupport(updateGameRequestDto.getPlayerSupport());
        }
        modelMapper.map(updateGameRequestDto, existingGame);
        gameRepository.save(existingGame);
        return Optional.of(modelMapper.map(existingGame, GameDto.class));
    }

    @Override
    public boolean deleteGame(String gameId) {
        Optional<Game> deleteGame = gameRepository.findById(UUID.fromString(gameId));
        if (deleteGame.isEmpty()) return false;
        gameRepository.delete(deleteGame.get());
        return true;
    }

    @Override
    public Optional<GameDto> activateGame(String gameId, boolean value) {
        Optional<Game> updateGame = gameRepository.findById(UUID.fromString(gameId));
        if (updateGame.isEmpty() || updateGame.get().getQuantity() == 0) return Optional.empty();

        Game activatingGame = updateGame.get();
        if (activatingGame.isActive() != value) {
            activatingGame.setActive(value);
            gameRepository.save(activatingGame);
            return Optional.of(modelMapper.map(activatingGame, GameDto.class));
        }

        return Optional.empty();
    }

    @Override
    public List<String> getAllGenres() {
        return gameRepository.getAllGenresList();
    }

    @Override
    public List<GameDto> getGamesByGenre(String genre) {
        List<Game> gamesByGenre = gameRepository.getGamesByGenre(genre);
        return modelMapper.map(gamesByGenre, new TypeToken<List<GameDto>>() {
        }.getType());
    }

    @Override
    public List<String> getAllPlayerSupport() {
        return gameRepository.getAllPlayerSupportList();
    }

    @Override
    public List<GameDto> getGamesByPlayerSupport(String playerSupport) {
        List<Game> gamesByPlayerSupport = gameRepository.getGamesByPlayerSupport(playerSupport);
        return modelMapper.map(gamesByPlayerSupport, new TypeToken<List<GameDto>>() {
        }.getType());
    }

    @Override
    public Optional<ReviewDto> createReview(String gameId, String userId, CreateOrUpdateReviewRequestDto reviewDto) {
        Review review = modelMapper.map(reviewDto, Review.class);
        Optional<User> optUser = userRepository.findById(UUID.fromString(userId));
        Optional<Game> optGame = gameRepository.findById(UUID.fromString(gameId));
        if (optUser.isEmpty() || optGame.isEmpty()) return Optional.empty();
        review.setUserId(optUser.get());
        review.setGameId(optGame.get());
        Review savedReview = reviewRepository.save(review);
        updateGameRating(optGame.get());
        return Optional.of(modelMapper.map(savedReview, ReviewDto.class));
    }

    @Override
    public Optional<ReviewDto> updateReview(String reviewId, String userId, CreateOrUpdateReviewRequestDto createOrUpdateReviewRequestDto) {
        Optional<Review> optionalUpdatingReview = reviewRepository.findById(UUID.fromString(reviewId));
        Optional<User> optUser = userRepository.findById(UUID.fromString(userId));
        if (optUser.isEmpty()) return Optional.empty();
        if (optionalUpdatingReview.isEmpty() || !optionalUpdatingReview.get().getUserId().getId().equals(optUser.get().getId()))
            return Optional.empty();
        Review updatedReview = optionalUpdatingReview.get();
        Game updatingGame = gameRepository.findById(updatedReview.getGameId().getId())
                .orElseThrow(() -> new NoSuchElementException("Game not found with ID: " + updatedReview.getGameId().getId()));
        updatedReview.setDescription(createOrUpdateReviewRequestDto.getDescription());
        updatedReview.setStarRating(createOrUpdateReviewRequestDto.getStarRating());
        reviewRepository.save(updatedReview);
        updateGameRating(updatingGame);
        return Optional.of(modelMapper.map(updatedReview, ReviewDto.class));
    }

    @Override
    public boolean deleteReview(String reviewId, String userId) {
        Optional<User> optUser = userRepository.findById(UUID.fromString(userId));
        if (optUser.isEmpty()) return false;
        Optional<Review> optionalDeletingReview = reviewRepository.findById(UUID.fromString(reviewId));
        if (optionalDeletingReview.isEmpty()
                || !optionalDeletingReview.get().getUserId().getId().equals(optUser.get().getId()))
            return false;
        Game updatingGame = gameRepository.findById(optionalDeletingReview.get().getGameId().getId())
                .orElseThrow(() -> new NoSuchElementException("Game not found with ID: " + optionalDeletingReview.get().getGameId().getId()));
        reviewRepository.delete(optionalDeletingReview.get());
        updateGameRating(updatingGame);
        return true;
    }

    @Override
    public Optional<KeyCreationDto> addKeyToGame(String gameId) {
        Game game = gameRepository.findById(UUID.fromString(gameId))
                .orElseThrow(() -> new RuntimeException("Game not found with ID: " + gameId));
        Key key = new Key();
        key.setGame(game);
        keyRepository.save(key);
        game.setQuantity(game.getQuantity() + 1);
        gameRepository.save(game);
        return Optional.of(modelMapper.map(key, KeyCreationDto.class));
    }

    @Override
    public Optional<Integer> countGameKeys(String gameId) {
        UUID convertedGameId = UUID.fromString(gameId);
        return gameRepository.getGameKeysAmount(convertedGameId);
    }

    private void updateGameRating(Game game) {
        Optional<Float> newAverageRating = reviewRepository.averageRating(game.getId());
        game.setAverageRating(newAverageRating.orElseThrow(() ->
                new NoSuchElementException("Average rating for the game could not be calculated!")));
        gameRepository.save(game);
    }
}
