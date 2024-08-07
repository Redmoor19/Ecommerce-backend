package com.example.gameStore.services;

import com.example.gameStore.dtos.UserDtos.CreateUserRequestDto;
import com.example.gameStore.dtos.UserDtos.UpdateUserRequestDto;
import com.example.gameStore.dtos.UserDtos.UserDto;
import com.example.gameStore.entities.User;
import com.example.gameStore.enums.UserStatus;
import com.example.gameStore.services.interfaces.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    private List<User> users = new ArrayList<>();
    private final ModelMapper modelMapper = new ModelMapper();

    public List<UserDto> getUsers() {
        return users.stream().map(user -> modelMapper.map(user, UserDto.class)).toList();
    }

    public Optional<UserDto> getUserById(UUID id) {
        Optional<User> foundUser = users.stream().filter(user -> user.getId().equals(id)).findFirst();
        return foundUser.map(user -> modelMapper.map(user, UserDto.class));
    }

    public Optional<UserDto> createUser(CreateUserRequestDto newUser) {
        User createUser = modelMapper.map(newUser, User.class);
        createUser.setId(UUID.randomUUID());
        createUser.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        createUser.setActiveStatus(UserStatus.UNVERIFIED);
        users.add(createUser);
        return Optional.of(modelMapper.map(createUser, UserDto.class));
    }

    public boolean deleteUser(UUID id) {
        users = users.stream().filter(user -> user.getId().equals(id)).toList();
        return true;
    }

    public Optional<UserDto> updateUser(UpdateUserRequestDto updateUserDto) {
        User updateUser = modelMapper.map(updateUserDto, User.class);
        users = users.stream().map(user ->
                user.getId().equals(updateUser.getId()) ? updateUser : user
        ).toList();
        return getUserById(updateUser.getId());
    }

    public Optional<UserDto> getCurrentUser() {
        return Optional.empty();
    }
}
