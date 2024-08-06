package com.example.gameStore.services;

import com.example.gameStore.dtos.UserDtos.CreateUserRequestDto;
import com.example.gameStore.dtos.UserDtos.LoggedInUserDto;
import com.example.gameStore.dtos.UserDtos.LoginUserRequestDto;
import com.example.gameStore.dtos.UserDtos.ResetPasswordRequestDto;
import com.example.gameStore.dtos.UserDtos.UpdatePasswordRequestDto;
import com.example.gameStore.dtos.UserDtos.UserDto;
import com.example.gameStore.services.interfaces.AuthService;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class AuthServiceImpl implements AuthService {

    UserDto userDto = new UserDto();
    LoggedInUserDto loggedInUserDto = new LoggedInUserDto("sdauhqwfiyqbwfy8asbf", userDto);

    public Optional<LoggedInUserDto> registerUser(CreateUserRequestDto newUser) {
        return Optional.of(loggedInUserDto);
    }

    public Optional<LoggedInUserDto> logUserIn(LoginUserRequestDto userCreds) {
        return Optional.of(loggedInUserDto);
    }

    public Optional<String> forgotPassword(String email) {
        return Optional.empty();
    }

    public Optional<LoggedInUserDto> resetPassword(String resetToken, UUID userID, ResetPasswordRequestDto resetPasswordDto) {
        return Optional.of(loggedInUserDto);
    }

    public Optional<LoggedInUserDto> updatePassword(UUID userId, UpdatePasswordRequestDto updatePasswordDto) {
        return Optional.of(loggedInUserDto);
    }

    public Optional<LoggedInUserDto> verifyEmail(String token) {
        return Optional.of(loggedInUserDto);
    }
}
