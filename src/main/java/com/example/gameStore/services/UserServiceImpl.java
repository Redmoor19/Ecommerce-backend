package com.example.gameStore.services;

import com.example.gameStore.dtos.UserDtos.CreateUserRequestDto;
import com.example.gameStore.dtos.UserDtos.UpdateUserRequestDto;
import com.example.gameStore.dtos.UserDtos.UserDto;
import com.example.gameStore.entities.User;
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
    private final ModelMapper modelMapper = new ModelMapper();

    public List<UserDto> getUsers() {
        var users = userRepository.findAll();
        return users.stream().map(user -> modelMapper.map(user, UserDto.class)).toList();
    }

    public Optional<UserDto> getUserById(UUID id) {
        Optional<User> foundUser = userRepository.findById(id);
        return foundUser.map(user -> modelMapper.map(user, UserDto.class));
    }

    public Optional<UserDto> createUser(CreateUserRequestDto newUser) {
        User createUser = modelMapper.map(newUser, User.class);
        System.out.println(createUser.toString());
        userRepository.save(createUser);
        return Optional.of(modelMapper.map(createUser, UserDto.class));
    }

    public boolean deleteUser(UUID id) {
        userRepository.deleteById(id);
        return true;
    }

    public Optional<UserDto> updateUser(UpdateUserRequestDto updateUserDto) {
        User updateUser = modelMapper.map(updateUserDto, User.class);
        userRepository.save(updateUser);
        return getUserById(updateUser.getId());
    }

    public Optional<UserDto> getCurrentUser() {
        return Optional.empty();
    }
}
