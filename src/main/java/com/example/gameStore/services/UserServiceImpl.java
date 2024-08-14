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
        Optional<User> foundUser = userRepository.findById(UUID.fromString(id));
        return foundUser.map(user -> modelMapper.map(user, UserDto.class));
    }

    public Optional<UserDto> createUser(CreateUserRequestDto newUser) {
        User user = modelMapper.map(newUser, User.class);
        User createdUser = userRepository.save(user);
        return Optional.of(modelMapper.map(createdUser, UserDto.class));
    }

    public Optional<UserDto> updateUser(UpdateUserRequestDto updateUserDto, String userId) {
        Optional<User> optUser = userRepository.findById(UUID.fromString(userId));
        if (optUser.isEmpty()) return Optional.empty();
        User user = optUser.get();
        modelMapper.map(updateUserDto, user);
        User savedUser = userRepository.save(user);
        return Optional.of(modelMapper.map(savedUser, UserDto.class));
    }

    public boolean updateUserRole(UpdateUserRoleRequestDto roleDto, String userId) {
        String role = roleDto.getRole();
        boolean isValid = UserRole.isValidRole(role);
        if (!isValid) return false;
        UserRole userRole = UserRole.valueOf(role.toUpperCase());

        return userRepository.updateUserRole(UUID.fromString(userId), userRole) > 0;
    }

    public boolean deleteUser(String id) {
        return userRepository.updateUserStatus(UUID.fromString(id), UserStatus.NOT_ACTIVE) > 0;
    }

    public boolean activateUser(String id) {
        return userRepository.updateUserStatus(UUID.fromString(id), UserStatus.ACTIVE) > 0;
    }

    public Optional<UserDto> getCurrentUser() {
        return Optional.empty();
    }

    public Optional<List<GameDto>> getFavouriteGames(String userId) {
        List<Game> games = favouriteUserGameRepository.getUserFavourites(UUID.fromString(userId));
        List<GameDto> gameDtos = games.stream().map(game -> modelMapper.map(game, GameDto.class)).toList();
        return Optional.of(gameDtos);
    }

    public boolean addFavouriteGame(String userId, String gameId) {
        Optional<Game> game = gameRepository.findById(UUID.fromString(gameId));
        Optional<User> user = userRepository.findById(UUID.fromString(userId));

        if (game.isEmpty() || user.isEmpty()) return false;

        FavouriteUserGame favourite = new FavouriteUserGame();
        favourite.setId(new FavouriteUserGameId(user.get().getId(), game.get().getId()));
        favourite.setUser(user.get());
        favourite.setGame(game.get());

        FavouriteUserGame saved = favouriteUserGameRepository.save(favourite);

        return saved.getGame().getId().equals(game.get().getId());
    }

    public boolean removeFavouriteGame(String userId, String gameId) {
        int deletedRows = favouriteUserGameRepository.deleteUserFavourite(UUID.fromString(userId), UUID.fromString(gameId));
        return deletedRows > 0;
    }
}
