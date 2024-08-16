package com.example.gameStore.services.interfaces;

import com.example.gameStore.dtos.UserDtos.CreateUserRequestDto;
import com.example.gameStore.dtos.UserDtos.ForgotPasswordUserDto;
import com.example.gameStore.dtos.UserDtos.LoggedInUserDto;
import com.example.gameStore.dtos.UserDtos.LoginUserRequestDto;
import com.example.gameStore.dtos.UserDtos.ResetPasswordRequestDto;
import com.example.gameStore.dtos.UserDtos.UpdatePasswordRequestDto;

import javax.naming.AuthenticationException;
import java.util.Optional;


public interface AuthService {
    public Optional<LoggedInUserDto> registerUser(CreateUserRequestDto newUser, String url);

    public Optional<LoggedInUserDto> logUserIn(LoginUserRequestDto userCreds) throws AuthenticationException;

    public boolean forgotPassword(ForgotPasswordUserDto forgotPasswordUserDto, String hostUrl);

    public Optional<LoggedInUserDto> resetPassword(String resetToken, ResetPasswordRequestDto resetPasswordDto);

    public Optional<LoggedInUserDto> updatePassword(String userId, UpdatePasswordRequestDto updatePasswordDto);

    public Optional<LoggedInUserDto> verifyEmail(String token);

    boolean sendVerificationToken(String userId, String hostUrl);
}
