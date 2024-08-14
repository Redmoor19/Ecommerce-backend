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
import com.example.gameStore.shared.TokenManager;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JWTServiceImpl jwtService;


    public Optional<LoggedInUserDto> registerUser(CreateUserRequestDto newUser) {
        boolean passwordMatch = comparePasswords(newUser.getPassword(), newUser.getConfirmPassword());
        if (!passwordMatch) {
            // Throw error password mismatch
            return Optional.empty();
        }
        User user = new User();
        modelMapper.map(newUser, user);

        String hashedPassword = passwordEncoder.encode(newUser.getPassword());
        user.setPassword(hashedPassword);

        String token = TokenManager.generateRandomToken();
        String hashedToken = TokenManager.hashToken(token);
        user.setConfirmEmailToken(hashedToken);
        User savedUser = userRepository.save(user);

        String jwtToken = jwtService.generateToken(savedUser);

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(newUser.getEmail(), newUser.getPassword()));
        // Send token to email using verification token

        return Optional.of(new LoggedInUserDto(jwtToken, modelMapper.map(savedUser, UserDto.class)));
    }

    public Optional<LoggedInUserDto> logUserIn(LoginUserRequestDto userCreds) {
        Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userCreds.getEmail(), userCreds.getPassword()));

        if (!auth.isAuthenticated()) return Optional.empty();

        Optional<User> optUser = userRepository.findByEmail(userCreds.getEmail());
        if (optUser.isEmpty()) {
            //Throw error incorrect email (user not found)
            return Optional.empty();
        }
        User user = optUser.get();
        String jwt = jwtService.generateToken(user);
        LoggedInUserDto loggedInUserDto = new LoggedInUserDto(jwt, modelMapper.map(user, UserDto.class));
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

        user.setPassword(passwordEncoder.encode(resetPasswordDto.getPassword()));
        user.setPasswordChangedAt(Timestamp.from(Instant.now()));
        user.setPasswordResetToken(null);
        user.setPasswordResetExpires(null);
        User savedUser = userRepository.save(user);

        String jwt = jwtService.generateToken(user);
        LoggedInUserDto loggedInUserDto = new LoggedInUserDto(jwt, modelMapper.map(savedUser, UserDto.class));
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

        user.setPassword(passwordEncoder.encode(updatePasswordDto.getNewPassword()));
        user.setPasswordChangedAt(Timestamp.from(Instant.now()));
        User savedUser = userRepository.save(user);

        String jwt = jwtService.generateToken(user);
        LoggedInUserDto loggedInUserDto = new LoggedInUserDto(jwt, modelMapper.map(savedUser, UserDto.class));
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

        String jwt = jwtService.generateToken(user);
        LoggedInUserDto loggedInUserDto = new LoggedInUserDto(jwt, modelMapper.map(savedUser, UserDto.class));
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
