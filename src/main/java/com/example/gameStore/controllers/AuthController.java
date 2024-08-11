package com.example.gameStore.controllers;

import com.example.gameStore.dtos.UserDtos.CreateUserRequestDto;
import com.example.gameStore.dtos.UserDtos.ForgotPasswordUserDto;
import com.example.gameStore.dtos.UserDtos.LoggedInUserDto;
import com.example.gameStore.dtos.UserDtos.LoginUserRequestDto;
import com.example.gameStore.dtos.UserDtos.ResetPasswordRequestDto;
import com.example.gameStore.dtos.UserDtos.UpdatePasswordRequestDto;
import com.example.gameStore.services.interfaces.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("api/v1/")
@AllArgsConstructor
public class AuthController {

    @Autowired
    private final AuthService authService;

    @PostMapping("auth/signup")
    public ResponseEntity<LoggedInUserDto> signUpUser(@RequestBody CreateUserRequestDto createUserRequestDto) {
        Optional<LoggedInUserDto> optionalLoggedInUserDto = authService.registerUser(createUserRequestDto);
        return optionalLoggedInUserDto.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @PostMapping("auth/login")
    public ResponseEntity<LoggedInUserDto> loginUser(@RequestBody LoginUserRequestDto loginUserRequestDto) {
        Optional<LoggedInUserDto> optionalLoggedInUserDto = authService.logUserIn(loginUserRequestDto);
        return optionalLoggedInUserDto.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @PostMapping("users/forgot-password")
    public ResponseEntity<String> forgotUserPassword(@RequestBody ForgotPasswordUserDto forgotPasswordUserDto) {
        Optional<String> optUrl = authService.forgotPassword(forgotPasswordUserDto);
        return optUrl.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PatchMapping("auth/reset-password/{token}")
    public ResponseEntity<LoggedInUserDto> resetUserPassword(@PathVariable String token, @RequestBody ResetPasswordRequestDto resetPasswordRequestDto) {
        Optional<LoggedInUserDto> optionalLoggedInUserDto = authService.resetPassword(token, resetPasswordRequestDto);
        return optionalLoggedInUserDto.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @PostMapping("users/me/update-password")
    public ResponseEntity<LoggedInUserDto> updateLoggedUserPassword(@RequestBody UpdatePasswordRequestDto updatePasswordRequestDto) {
        String userId = "sdasfasg";
        Optional<LoggedInUserDto> optionalLoggedInUserDto = authService.updatePassword(userId, updatePasswordRequestDto);
        return optionalLoggedInUserDto.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @PostMapping("auth/verify/{token}")
    public ResponseEntity<LoggedInUserDto> verifyUserEmail(@PathVariable String token) {
        Optional<LoggedInUserDto> optionalLoggedInUserDto = authService.verifyEmail(token);
        return optionalLoggedInUserDto.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.badRequest().build());
    }
}
