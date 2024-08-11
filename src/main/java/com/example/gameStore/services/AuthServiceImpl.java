package com.example.gameStore.services;

import com.example.gameStore.dtos.UserDtos.CreateUserRequestDto;
import com.example.gameStore.dtos.UserDtos.ForgotPasswordUserDto;
import com.example.gameStore.dtos.UserDtos.LoggedInUserDto;
import com.example.gameStore.dtos.UserDtos.LoginUserRequestDto;
import com.example.gameStore.dtos.UserDtos.ResetPasswordRequestDto;
import com.example.gameStore.dtos.UserDtos.UpdatePasswordRequestDto;
import com.example.gameStore.dtos.UserDtos.UserDto;
import com.example.gameStore.entities.User;
import com.example.gameStore.enums.UserStatus;
import com.example.gameStore.repositories.UserRepository;
import com.example.gameStore.services.interfaces.AuthService;
import com.example.gameStore.shared.PasswordAuthentication;
import com.example.gameStore.shared.TokenManager;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthServiceImpl implements AuthService {

    private final ModelMapper modelMapper = new ModelMapper();
    @Autowired
    private UserRepository userRepository;


    public Optional<LoggedInUserDto> registerUser(CreateUserRequestDto newUser) {
        boolean passwordMatch = comparePasswords(newUser.getPassword(), newUser.getConfirmPassword());
        if (!passwordMatch) {
            // Throw error password mismatch
            return Optional.empty();
        }
        User user = new User();
        modelMapper.map(newUser, user);

        String hashedPassword = PasswordAuthentication.hash(newUser.getPassword());
        user.setPassword(hashedPassword);

        String token = TokenManager.generateRandomToken();
        String hashedToken = TokenManager.hashToken(token);
        user.setConfirmEmailToken(hashedToken);
        User savedUser = userRepository.save(user);

        // Send token to email using verification token

        LoggedInUserDto loggedInUserDto = new LoggedInUserDto("Tokena poka net", modelMapper.map(savedUser, UserDto.class));
        return Optional.of(loggedInUserDto);
    }

    public Optional<LoggedInUserDto> logUserIn(LoginUserRequestDto userCreds) {
        Optional<User> optUser = userRepository.findByEmail(userCreds.getEmail());
        if (optUser.isEmpty()) {
            //Throw error incorrect email (user not found)
            return Optional.empty();
        }
        User user = optUser.get();

        boolean passwordCorrect = PasswordAuthentication.verifyHash(userCreds.getPassword(), user.getPassword());
        if (!passwordCorrect) {
            //Throw error password incorrect
            return Optional.empty();
        }

        LoggedInUserDto loggedInUserDto = new LoggedInUserDto("Tokena poka net", modelMapper.map(user, UserDto.class));
        return Optional.of(loggedInUserDto);
    }

    // This method will return boolean after email service implemented, since token will be sent to email
    public Optional<String> forgotPassword(ForgotPasswordUserDto forgotPasswordUserDto) {
        String email = forgotPasswordUserDto.getEmail();
        Optional<User> optUser = userRepository.findByEmail(email);
        if (optUser.isEmpty()) {
            //Throw error incorrect email (user not found)
            return Optional.empty();
        }
        User user = optUser.get();

        String token = TokenManager.generateRandomToken();
        // Email service, send an url, IMPLEMENT AFTER SERVICE IS AVAILABLE
        String hashedToken = TokenManager.hashToken(token);

        Instant now = Instant.now();
        Duration duration = Duration.ofMinutes(15);
        Instant futureInstant = now.plus(duration);

        user.setPasswordResetToken(hashedToken);
        user.setPasswordResetExpires(Timestamp.from(futureInstant));

        userRepository.save(user);
        return Optional.of(token);
    }

    public Optional<LoggedInUserDto> resetPassword(String resetToken, ResetPasswordRequestDto resetPasswordDto) {
        boolean passwordMatch = comparePasswords(resetPasswordDto.getPassword(), resetPasswordDto.getConfirmPassword());
        if (!passwordMatch) {
            // Throw error password mismatch
            return Optional.empty();
        }

        String hashedToken = TokenManager.hashToken(resetToken);
        Optional<User> optUser = userRepository.findByPasswordResetToken(hashedToken);
        if (optUser.isEmpty()) {
            //Throw error incorrect token user not found
            return Optional.empty();
        }
        User user = optUser.get();
        if (user.getPasswordResetExpires().after(Timestamp.from(Instant.now()))) {
            //Throw error token is expired
            return Optional.empty();
        }

        String hashedPassword = PasswordAuthentication.hash(resetPasswordDto.getPassword());
        user.setPassword(hashedPassword);
        user.setPasswordChangedAt(Timestamp.from(Instant.now()));
        user.setPasswordResetToken(null);
        user.setPasswordResetExpires(null);
        User savedUser = userRepository.save(user);

        LoggedInUserDto loggedInUserDto = new LoggedInUserDto("Tokena poka net", modelMapper.map(savedUser, UserDto.class));
        return Optional.of(loggedInUserDto);
    }

    public Optional<LoggedInUserDto> updatePassword(String userId, UpdatePasswordRequestDto updatePasswordDto) {
        boolean passwordMatch = comparePasswords(updatePasswordDto.getNewPassword(), updatePasswordDto.getNewPasswordConfirm());
        if (!passwordMatch) {
            // Throw error password mismatch
            return Optional.empty();
        }

        Optional<User> optUser = userRepository.findById(UUID.fromString(userId));
        if (optUser.isEmpty()) {
            //Throw error user not found (very unlikely, but still, as John Lennon once said let it be)
            return Optional.empty();
        }
        User user = optUser.get();

        String hashedPassword = PasswordAuthentication.hash(updatePasswordDto.getNewPassword());
        user.setPassword(hashedPassword);
        user.setPasswordChangedAt(Timestamp.from(Instant.now()));
        User savedUser = userRepository.save(user);
        userRepository.save(user);

        LoggedInUserDto loggedInUserDto = new LoggedInUserDto("Tokena poka net", modelMapper.map(savedUser, UserDto.class));
        return Optional.of(loggedInUserDto);
    }

    public Optional<LoggedInUserDto> verifyEmail(String token) {
        String hashedToken = TokenManager.hashToken(token);

        Optional<User> optUser = userRepository.findByConfirmEmailToken(hashedToken);
        if (optUser.isEmpty()) {
            //Throw error invalid token
            return Optional.empty();
        }
        User user = optUser.get();
        if (user.getActiveStatus() != UserStatus.UNVERIFIED) {
            //Throw error user status incorrect
            return Optional.empty();
        }

        user.setActiveStatus(UserStatus.ACTIVE);
        user.setConfirmEmailToken(null);
        User savedUser = userRepository.save(user);

        LoggedInUserDto loggedInUserDto = new LoggedInUserDto("Tokena poka net", modelMapper.map(savedUser, UserDto.class));
        return Optional.of(loggedInUserDto);
    }

    //No route implemented yet
    public boolean sendVerificationToken(String userId) {
        Optional<User> optUser = userRepository.findById(UUID.fromString(userId));
        if (optUser.isEmpty()) {
            //Throw error user not found
            return false;
        }
        User user = optUser.get();

        String token = TokenManager.generateRandomToken();
        String hashedToken = TokenManager.hashToken(token);
        user.setConfirmEmailToken(hashedToken);
        // Email service, send an url, IMPLEMENT AFTER SERVICE IS AVAILABLE

        return true;
    }

    private boolean comparePasswords(String password, String passwordConfirm) {
        return password.equals(passwordConfirm);
    }
}
