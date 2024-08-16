package com.example.gameStore.services;

import com.example.gameStore.dtos.GameDtos.GameDto;
import com.example.gameStore.dtos.UserDtos.CreateUserRequestDto;
import com.example.gameStore.dtos.UserDtos.UpdateUserRequestDto;
import com.example.gameStore.dtos.UserDtos.UpdateUserRoleRequestDto;
import com.example.gameStore.dtos.UserDtos.UserDto;
import com.example.gameStore.entities.FavouriteUserGame;
import com.example.gameStore.entities.FavouriteUserGameId;
import com.example.gameStore.entities.Game;
import com.example.gameStore.entities.User;
import com.example.gameStore.enums.UserRole;
import com.example.gameStore.enums.UserStatus;
import com.example.gameStore.repositories.FavouriteUserGameRepository;
import com.example.gameStore.repositories.GameRepository;
import com.example.gameStore.repositories.UserRepository;
import com.example.gameStore.services.interfaces.UserService;
import com.example.gameStore.shared.exceptions.ResourceAlreadyExistsException;
import com.example.gameStore.shared.exceptions.ResourceNotFoundException;
import com.example.gameStore.utilities.TypeConverter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FavouriteUserGameRepository favouriteUserGameRepository;
    @Autowired
    private GameRepository gameRepository;

    private final ModelMapper modelMapper = new ModelMapper();

    public List<UserDto> getUsers() {
        var users = userRepository.findAll();
        return users.stream().map(user -> modelMapper.map(user, UserDto.class)).toList();
    }

    public Optional<UserDto> getUserById(String id) {
        UUID userId = TypeConverter.convertStringToUUID(id);
        Optional<User> foundUser = userRepository.findById(userId);
        return foundUser.map(user -> modelMapper.map(user, UserDto.class));
    }

    public Optional<UserDto> createUser(CreateUserRequestDto newUser) {
        Optional<User> foundUser = userRepository.findByEmail(newUser.getEmail());
        if (foundUser.isPresent())
            throw new ResourceAlreadyExistsException("User with " + newUser.getEmail() + " already exists");

        User user = modelMapper.map(newUser, User.class);
        User createdUser = userRepository.save(user);
        return Optional.of(modelMapper.map(createdUser, UserDto.class));
    }

    public Optional<UserDto> updateUser(UpdateUserRequestDto updateUserDto, String id) {
        UUID userId = TypeConverter.convertStringToUUID(id);
        Optional<User> optUser = userRepository.findById(userId);
        if (optUser.isEmpty()) throw new ResourceNotFoundException("User with such Id not found");
        User user = optUser.get();
        if (!user.getEmail().equals(updateUserDto.getEmail())) {
            user.setActiveStatus(UserStatus.UNVERIFIED);
        }
        modelMapper.map(updateUserDto, user);
        User savedUser = userRepository.save(user);
        return Optional.of(modelMapper.map(savedUser, UserDto.class));
    }

    public boolean updateUserRole(UpdateUserRoleRequestDto roleDto, String id) {
        String role = roleDto.getRole();
        UUID userId = TypeConverter.convertStringToUUID(id);
        boolean isValid = UserRole.isValidRole(role);
        if (!isValid) throw new IllegalArgumentException("Not valid user role");
        UserRole userRole = UserRole.valueOf(role.toUpperCase());

        return userRepository.updateUserRole(userId, userRole) > 0;
    }

    public boolean deleteUser(String id) {
        UUID userId = TypeConverter.convertStringToUUID(id);
        return userRepository.updateUserStatus(userId, UserStatus.NOT_ACTIVE) > 0;
    }

    public boolean activateUser(String id) {
        UUID userId = TypeConverter.convertStringToUUID(id);
        return userRepository.updateUserStatus(userId, UserStatus.ACTIVE) > 0;
    }

    public List<GameDto> getFavouriteGames(String id) {
        UUID userId = TypeConverter.convertStringToUUID(id);
        List<Game> games = favouriteUserGameRepository.getUserFavourites(userId);
        return games.stream().map(game -> modelMapper.map(game, GameDto.class)).toList();
    }

    public boolean addFavouriteGame(String userId, String gameId) {
        UUID userUUID = TypeConverter.convertStringToUUID(userId);
        UUID gameUUID = TypeConverter.convertStringToUUID(gameId);

        Game game = gameRepository.findById(gameUUID).orElseThrow(() -> new ResourceNotFoundException("Game with id " + gameId + " not found"));
        User user = userRepository.findById(userUUID).orElseThrow(() -> new ResourceNotFoundException("User with id " + userId + " not found"));

        FavouriteUserGame favourite = new FavouriteUserGame();
        favourite.setId(new FavouriteUserGameId(user.getId(), game.getId()));
        favourite.setUser(user);
        favourite.setGame(game);

        FavouriteUserGame saved = favouriteUserGameRepository.save(favourite);

        return saved.getGame().getId().equals(game.getId());
    }

    public boolean removeFavouriteGame(String userId, String gameId) {
        UUID userUUID = TypeConverter.convertStringToUUID(userId);
        UUID gameUUID = TypeConverter.convertStringToUUID(gameId);

        return favouriteUserGameRepository.deleteUserFavourite(userUUID, gameUUID) > 0;
    }
}
