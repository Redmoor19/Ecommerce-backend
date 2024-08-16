package com.example.gameStore.services;

import com.example.gameStore.dtos.GameDtos.CreateGameRequestDto;
import com.example.gameStore.dtos.GameDtos.GameDto;
import com.example.gameStore.dtos.GameDtos.GamesListHeaderDto;
import com.example.gameStore.dtos.GameDtos.GamesListResponseDto;
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
import com.example.gameStore.enums.Genre;
import com.example.gameStore.enums.PlayerSupport;
import com.example.gameStore.repositories.GameRepository;
import com.example.gameStore.repositories.KeyRepository;
import com.example.gameStore.repositories.ReviewRepository;
import com.example.gameStore.repositories.UserRepository;
import com.example.gameStore.services.interfaces.GameService;
import com.example.gameStore.shared.exceptions.BadRequestException;
import com.example.gameStore.shared.exceptions.NoContentException;
import com.example.gameStore.shared.exceptions.ResourceNotFoundException;
import com.example.gameStore.utilities.GameSpecification;
import com.example.gameStore.utilities.TypeConverter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.Arrays;
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
        return !list1.equals(list2);
    }

    @Override
    public GamesListResponseDto findAllGames(String sortField, String sortOrder, int pageNumber, int pageSize, String searchKeyword, List<String> genres, List<String> playerSupport) {
        isSortFieldValid(sortField);

        if (pageSize < 1) throw new IllegalArgumentException("Page size must be greater than zero.");
        Sort.Direction direction = Sort.Direction.fromString(sortOrder);
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, Sort.by(direction, sortField));

        List<Genre> genreList = Optional.ofNullable(genres)
                .filter(list -> !list.isEmpty())
                .map(list -> list.stream()
                        .map(Genre::valueOf)
                        .toList())
                .orElseGet(Genre::getAllGenres);

        List<PlayerSupport> supportList = Optional.ofNullable(playerSupport)
                .filter(list -> !list.isEmpty())
                .map(list -> list.stream()
                        .map(PlayerSupport::valueOf)
                        .toList())
                .orElseGet(PlayerSupport::getAllPlayerSupport);

        Specification<Game> spec = GameSpecification.withFilters(searchKeyword, genreList, supportList);
        Page<Game> gamesPage = gameRepository.findAll(spec, pageable);
        List<GameDto> allGamesList = gamesPage.getContent()
                .stream()
                .map(game -> modelMapper.map(game, GameDto.class))
                .toList();

        if (allGamesList.isEmpty()) allGamesList = new ArrayList<>();

        Pageable pageableForPagination = PageRequest.of(0, 10000, Sort.by(direction, sortField));
        Page<Game> gamesPageForPagination = gameRepository.findAll(spec, pageableForPagination);

        int allGamesQuantity = gamesPageForPagination.getContent().size();
        int pagesQuantity = (int) Math.ceil((double) allGamesQuantity / pageSize);
        return new GamesListResponseDto(new GamesListHeaderDto(allGamesQuantity, pagesQuantity, (int) pageSize, (int) pageNumber), allGamesList);
    }

    @Override
    public GamesListResponseDto findAllActiveGames(String sortField, String sortOrder, int pageNumber, int pageSize, String searchKeyword, List<String> genres, List<String> playerSupport) {
        isSortFieldValid(sortField);

        if (pageSize < 1) throw new IllegalArgumentException("Page size must be greater than zero.");
        Sort.Direction direction = Sort.Direction.fromString(sortOrder);
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, Sort.by(direction, sortField));

        List<Genre> genreList = Optional.ofNullable(genres)
                .filter(list -> !list.isEmpty())
                .map(list -> list.stream()
                        .map(Genre::valueOf)
                        .toList())
                .orElseGet(Genre::getAllGenres);

        List<PlayerSupport> supportList = Optional.ofNullable(playerSupport)
                .filter(list -> !list.isEmpty())
                .map(list -> list.stream()
                        .map(PlayerSupport::valueOf)
                        .toList())
                .orElseGet(PlayerSupport::getAllPlayerSupport);

        Specification<Game> spec = Specification
                .where(GameSpecification.isActive())
                .and(GameSpecification.withFilters(searchKeyword, genreList, supportList));
        Page<Game> gamesPage = gameRepository.findAll(spec, pageable);

        List<GameDto> allGamesList = gamesPage.getContent()
                .stream()
                .map(game -> modelMapper.map(game, GameDto.class))
                .toList();

        if (allGamesList.isEmpty()) allGamesList = new ArrayList<>();

        Pageable pageableForPagination = PageRequest.of(0, 10000, Sort.by(direction, sortField));
        Page<Game> gamesPageForPagination = gameRepository.findAll(spec, pageableForPagination);

        int allGamesQuantity = gamesPageForPagination.getContent().size();
        int pagesQuantity = (int) Math.ceil((double) allGamesQuantity / pageSize);
        return new GamesListResponseDto(new GamesListHeaderDto(allGamesQuantity, pagesQuantity, (int) pageSize, (int) pageNumber), allGamesList);
    }

    @Override
    public Optional<SingleGameWithReviewsDto> getGameById(String id) {
        UUID gameId = TypeConverter.convertStringToUUID(id, "Invalid game id format: " + id);
        Optional<Game> game = gameRepository.findById(gameId);
        if (game.isEmpty()) throw new ResourceNotFoundException("Searching game not found!");

        return game.map(g -> {
            SingleGameWithReviewsDto singleGameWithReviewsDto = modelMapper.map(g, SingleGameWithReviewsDto.class);
            List<EmbeddedReviewDto> reviews = reviewRepository.findReviewsByGameId(gameId);
            singleGameWithReviewsDto.setReviews(reviews);
            return singleGameWithReviewsDto;
        });
    }

    @Override
    public Optional<GameDto> createGame(CreateGameRequestDto createGameRequestDto) {
        Game gameToCreate = modelMapper.map(createGameRequestDto, Game.class);
        Game savedGame = gameRepository.save(gameToCreate);
        return Optional.of(savedGame).map(game -> modelMapper.map(game, GameDto.class));
    }

    @Override
    public Optional<GameDto> updateGame(@RequestBody UpdateGameRequestDto updateGameRequestDto) {
        Optional<Game> updateGame = gameRepository.findById(updateGameRequestDto.getId());
        if (updateGame.isEmpty()) throw new ResourceNotFoundException("Updating game not found!");
        Game existingGame = updateGame.get();
        if (areNotEnumListsEquals(existingGame.getGenreList(), updateGameRequestDto.getGenreList())) {
            existingGame.setGenreList(updateGameRequestDto.getGenreList());
        }
        if (areNotEnumListsEquals(existingGame.getPlayerSupport(), updateGameRequestDto.getPlayerSupport())) {
            existingGame.setPlayerSupport(updateGameRequestDto.getPlayerSupport());
        }
        modelMapper.map(updateGameRequestDto, existingGame);
        Game savedGame = gameRepository.save(existingGame);
        return Optional.of(modelMapper.map(savedGame, GameDto.class));
    }

    @Override
    public Optional<GameDto> deactivateGame(String gameId) {
        Optional<Game> updateGame = gameRepository.findById(TypeConverter.convertStringToUUID(gameId));
        if (updateGame.isEmpty()) throw new ResourceNotFoundException("Deactivating game not found!");

        Game deactivatingGame = updateGame.get();
        if (deactivatingGame.isActive()) {
            deactivatingGame.setActive(false);
            Game savedGame = gameRepository.save(deactivatingGame);
            return Optional.of(modelMapper.map(savedGame, GameDto.class));
        }
        return Optional.empty();
    }

    @Override
    public Optional<GameDto> activateGame(String gameId) {
        Optional<Game> updateGame = gameRepository.findById(TypeConverter.convertStringToUUID(gameId));
        if (updateGame.isEmpty()) throw new ResourceNotFoundException("Activating game not found!");
        if (updateGame.get().getQuantity() == 0)
            throw new ResourceNotFoundException("There are no available keys for this game. Activation is not possible.");

        Game activatingGame = updateGame.get();
        if (!activatingGame.isActive()) {
            activatingGame.setActive(true);
            Game savedGame = gameRepository.save(activatingGame);
            return Optional.of(modelMapper.map(savedGame, GameDto.class));
        }
        throw new BadRequestException("The game is already active");
    }

    @Override
    public List<GameDto> getGamesByGenre(String genre) {
        Genre searchingGenre = Genre.fromString(genre);
        List<Game> gamesByGenre = gameRepository.getGamesByGenre(searchingGenre);
        return modelMapper.map(gamesByGenre, new TypeToken<List<GameDto>>() {
        }.getType());
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
        Optional<User> optUser = userRepository.findById(TypeConverter.convertStringToUUID(userId));
        Optional<Game> optGame = gameRepository.findById(TypeConverter.convertStringToUUID(gameId));
        if (optUser.isEmpty()) throw new ResourceNotFoundException("User not found!");
        if (optGame.isEmpty()) throw new ResourceNotFoundException("Game not found!");
        review.setUserId(optUser.get());
        review.setGameId(optGame.get());
        Review savedReview = reviewRepository.save(review);
        updateGameRating(optGame.get());
        return Optional.of(modelMapper.map(savedReview, ReviewDto.class));
    }

    @Override
    public Optional<ReviewDto> updateReview(String reviewId, String userId, CreateOrUpdateReviewRequestDto createOrUpdateReviewRequestDto) {
        Optional<Review> optionalUpdatingReview = reviewRepository.findById(TypeConverter.convertStringToUUID(reviewId));
        Optional<User> optUser = userRepository.findById(TypeConverter.convertStringToUUID(userId));
        if (optUser.isEmpty()) throw new ResourceNotFoundException("User not found!");
        if (optionalUpdatingReview.isEmpty()) throw new ResourceNotFoundException("Review not found!");
        if (!optionalUpdatingReview.get().getUserId().getId().equals(optUser.get().getId()))
            throw new BadRequestException("You don't have permission to edit this review.");
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
        Optional<User> optUser = userRepository.findById(TypeConverter.convertStringToUUID(userId));
        if (optUser.isEmpty()) throw new ResourceNotFoundException("User not found!");
        Optional<Review> optionalDeletingReview = reviewRepository.findById(TypeConverter.convertStringToUUID(reviewId));
        if (optionalDeletingReview.isEmpty()) throw new ResourceNotFoundException("Review not found!");
        if (!optionalDeletingReview.get().getUserId().getId().equals(optUser.get().getId()))
            throw new BadRequestException("You don't have permission to delete this review.");
        Game updatingGame = gameRepository.findById(optionalDeletingReview.get().getGameId().getId())
                .orElseThrow(() -> new NoSuchElementException("Game not found with ID: " + optionalDeletingReview.get().getGameId().getId()));
        reviewRepository.delete(optionalDeletingReview.get());
        updateGameRating(updatingGame);
        return true;
    }

    @Override
    public Optional<KeyCreationDto> addKeyToGame(String gameId) {
        Game game = gameRepository.findById(TypeConverter.convertStringToUUID(gameId))
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
        return gameRepository.getGameKeysAmount(TypeConverter.convertStringToUUID(gameId));
    }

    private void updateGameRating(Game game) {
        Optional<Float> newAverageRating = reviewRepository.averageRating(game.getId());
        game.setAverageRating(newAverageRating.orElse(0.0f));
        gameRepository.save(game);
    }

    private void isSortFieldValid(String sortField) {
        boolean isValidField = Arrays.stream(Game.class.getDeclaredFields())
                .anyMatch(f -> f.getName().equals(sortField));

        if (!isValidField) {
            throw new IllegalArgumentException("Invalid sort field: " + sortField);
        }
    }
}
