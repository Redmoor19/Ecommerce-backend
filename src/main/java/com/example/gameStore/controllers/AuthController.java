package com.example.gameStore.controllers;

import com.example.gameStore.dtos.GlobalResponse;
import com.example.gameStore.dtos.UserDtos.CreateUserRequestDto;
import com.example.gameStore.dtos.UserDtos.ForgotPasswordUserDto;
import com.example.gameStore.dtos.UserDtos.LoggedInUserDto;
import com.example.gameStore.dtos.UserDtos.LoginUserRequestDto;
import com.example.gameStore.dtos.UserDtos.ResetPasswordRequestDto;
import com.example.gameStore.dtos.UserDtos.UpdatePasswordRequestDto;
import com.example.gameStore.services.interfaces.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.naming.AuthenticationException;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/")
@AllArgsConstructor
public class AuthController {

    @Autowired
    private final AuthService authService;

    @PostMapping("auth/signup")
    public ResponseEntity<GlobalResponse<LoggedInUserDto>> signUpUser(@RequestBody @Valid CreateUserRequestDto createUserRequestDto, HttpServletRequest request) {
        Optional<LoggedInUserDto> optionalLoggedInUserDto = authService.registerUser(createUserRequestDto, request.getHeader("Origin"));
        return optionalLoggedInUserDto
                .map(loggedInUserDto -> ResponseEntity.ok(new GlobalResponse<>(loggedInUserDto)))
                .orElseThrow(() -> new RuntimeException("Something went wrong"));
    }

    @PostMapping("auth/login")
    public ResponseEntity<GlobalResponse<LoggedInUserDto>> loginUser(@RequestBody @Valid LoginUserRequestDto loginUserRequestDto) throws AuthenticationException {
        Optional<LoggedInUserDto> optionalLoggedInUserDto = authService.logUserIn(loginUserRequestDto);
        return optionalLoggedInUserDto
                .map(loggedInUserDto -> ResponseEntity.ok(new GlobalResponse<>(loggedInUserDto)))
                .orElseThrow(() -> new RuntimeException("Something went wrong"));
    }

    @PostMapping("auth/forgot-password")
    public ResponseEntity<GlobalResponse<Void>> forgotUserPassword(@RequestBody @Valid ForgotPasswordUserDto forgotPasswordUserDto, HttpServletRequest request) {
        if (!authService.forgotPassword(forgotPasswordUserDto, request.getHeader("Origin"))) {
            throw new RuntimeException("Something went wrong");
        }
        return ResponseEntity.ok(new GlobalResponse<>(null));
    }

    @PatchMapping("auth/reset-password/{token}")
    public ResponseEntity<GlobalResponse<LoggedInUserDto>> resetUserPassword(@PathVariable String token, @RequestBody @Valid ResetPasswordRequestDto resetPasswordRequestDto) {
        Optional<LoggedInUserDto> optionalLoggedInUserDto = authService.resetPassword(token, resetPasswordRequestDto);
        return optionalLoggedInUserDto
                .map(loggedInUserDto -> ResponseEntity.ok(new GlobalResponse<>(loggedInUserDto)))
                .orElseThrow(() -> new RuntimeException("Something went wrong"));
    }

    @PostMapping("users/me/update-password")
    public ResponseEntity<GlobalResponse<LoggedInUserDto>> updateLoggedUserPassword(@RequestBody @Valid UpdatePasswordRequestDto updatePasswordRequestDto, HttpServletRequest request) {
        String userId = (String) request.getAttribute("userId");
        Optional<LoggedInUserDto> optionalLoggedInUserDto = authService.updatePassword(userId, updatePasswordRequestDto);
        return optionalLoggedInUserDto
                .map(loggedInUserDto -> ResponseEntity.ok(new GlobalResponse<>(loggedInUserDto)))
                .orElseThrow(() -> new RuntimeException("Something went wrong"));
    }

    @PostMapping("auth/verify/{token}")
    public ResponseEntity<GlobalResponse<LoggedInUserDto>> verifyUserEmail(@PathVariable String token) {
        Optional<LoggedInUserDto> optionalLoggedInUserDto = authService.verifyEmail(token);
        return optionalLoggedInUserDto
                .map(loggedInUserDto -> ResponseEntity.ok(new GlobalResponse<>(loggedInUserDto)))
                .orElseThrow(() -> new RuntimeException("Something went wrong"));
    }

    @PostMapping("auth/verify/send-mail")
    public ResponseEntity<GlobalResponse<Void>> sendVerificationToken(HttpServletRequest request) {
        String userId = (String) request.getAttribute("userId");
        if (!authService.sendVerificationToken(userId, request.getHeader("Origin"))) {
            throw new RuntimeException("Something went wrong");
        }
        return ResponseEntity.ok(new GlobalResponse<>(null));
    }
}
