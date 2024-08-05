package com.example.gameStore.services.interfaces;

import com.example.gameStore.dtos.UserDtos.CreateUserRequestDto;
import com.example.gameStore.dtos.UserDtos.UpdateUserRequestDto;
import com.example.gameStore.dtos.UserDtos.UserDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {
    List<UserDto> getUsers();
    Optional<UserDto> getUserById(UUID id);
    UserDto createUser(CreateUserRequestDto newUser);
    boolean deleteUser(UUID id);
    UserDto updateUser(UpdateUserRequestDto updateUser);
    Optional<UserDto> getCurrentUser();
    UserDto updateCurrentUser(UpdateUserRequestDto updateUser);
    boolean deleteCurrentUser();
}
