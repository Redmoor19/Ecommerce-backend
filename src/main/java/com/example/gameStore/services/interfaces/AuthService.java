package com.example.gameStore.services.interfaces;

import com.example.gameStore.dtos.UserDtos.CreateUserRequestDto;
import com.example.gameStore.dtos.UserDtos.LoggedInUserDto;
import com.example.gameStore.dtos.UserDtos.LoginUserRequestDto;
import com.example.gameStore.dtos.UserDtos.ResetPasswordRequestDto;
import com.example.gameStore.dtos.UserDtos.UpdatePasswordRequestDto;

import java.util.Optional;
import java.util.UUID;


public interface AuthService {
    public Optional<LoggedInUserDto> registerUser(CreateUserRequestDto newUser);

    public Optional<LoggedInUserDto> logUserIn(LoginUserRequestDto userCreds);

    public Optional<String> forgotPassword(String email);

    public Optional<LoggedInUserDto> resetPassword(String resetToken, UUID userID, ResetPasswordRequestDto resetPasswordDto);

    public Optional<LoggedInUserDto> updatePassword(UUID userId, UpdatePasswordRequestDto updatePasswordDto);

    public Optional<LoggedInUserDto> verifyEmail(String token);
}
