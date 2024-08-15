package com.example.gameStore.services.interfaces;

import com.example.gameStore.dtos.GameDtos.GameDto;
import com.example.gameStore.dtos.UserDtos.CreateUserRequestDto;
import com.example.gameStore.dtos.UserDtos.UpdateUserRequestDto;
import com.example.gameStore.dtos.UserDtos.UpdateUserRoleRequestDto;
import com.example.gameStore.dtos.UserDtos.UserDto;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<UserDto> getUsers();

    Optional<UserDto> getUserById(String id);

    Optional<UserDto> createUser(CreateUserRequestDto newUser);

    Optional<UserDto> updateUser(UpdateUserRequestDto updateUser, String userId);

    boolean updateUserRole(UpdateUserRoleRequestDto role, String userId);

    boolean deleteUser(String id);

    boolean activateUser(String id);

    List<GameDto> getFavouriteGames(String userId);

    boolean addFavouriteGame(String userId, String gameId);

    boolean removeFavouriteGame(String userId, String gameId);
}
