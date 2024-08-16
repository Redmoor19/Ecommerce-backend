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
import com.example.gameStore.services.interfaces.EmailService;
import com.example.gameStore.services.interfaces.OrderService;
import com.example.gameStore.shared.TokenManager;
import com.example.gameStore.shared.exceptions.BadRequestException;
import com.example.gameStore.shared.exceptions.ResourceNotFoundException;
import com.example.gameStore.utilities.TypeConverter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;
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
    @Autowired
    private EmailService emailService;
    @Autowired
    private OrderService orderService;


    public Optional<LoggedInUserDto> registerUser(CreateUserRequestDto newUser, String hostUrl) {
        boolean passwordMatch = comparePasswords(newUser.getPassword(), newUser.getConfirmPassword());
        if (!passwordMatch) {
            throw new BadRequestException("Password must match with password confirm");
        }
        User user = modelMapper.map(newUser, User.class);

        String hashedPassword = passwordEncoder.encode(newUser.getPassword());
        user.setPassword(hashedPassword);

        String activationToken = TokenManager.generateRandomToken();
        String hashedActivationToken = TokenManager.hashToken(activationToken);
        user.setConfirmEmailToken(hashedActivationToken);
        User savedUser = userRepository.save(user);

        emailService.sendMessageAccountVerification(newUser.getEmail(), activationToken, hostUrl);
        orderService.createNewOrder(savedUser);

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(newUser.getEmail(), newUser.getPassword()));

        String jwtToken = jwtService.generateToken(savedUser);
        return Optional.of(new LoggedInUserDto(jwtToken, modelMapper.map(savedUser, UserDto.class)));
    }

    public Optional<LoggedInUserDto> logUserIn(LoginUserRequestDto userCreds) throws AuthenticationException {
        Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userCreds.getEmail(), userCreds.getPassword()));

        if (!auth.isAuthenticated()) throw new AuthenticationException("Wrong credentials");

        Optional<User> optUser = userRepository.findByEmail(userCreds.getEmail());
        if (optUser.isEmpty()) throw new BadRequestException("No user with email: " + userCreds.getEmail() + " found");
        User user = optUser.get();

        String jwt = jwtService.generateToken(user);
        LoggedInUserDto loggedInUserDto = new LoggedInUserDto(jwt, modelMapper.map(user, UserDto.class));
        return Optional.of(loggedInUserDto);
    }

    public boolean forgotPassword(ForgotPasswordUserDto forgotPasswordUserDto, String hostUrl) {
        String email = forgotPasswordUserDto.getEmail();
        Optional<User> optUser = userRepository.findByEmail(email);
        if (optUser.isEmpty())
            throw new BadRequestException("No user with email: " + forgotPasswordUserDto.getEmail() + " found");
        User user = optUser.get();

        String token = TokenManager.generateRandomToken();
        emailService.sendMessagePasswordReset(forgotPasswordUserDto.getEmail(), token, hostUrl);

        String hashedToken = TokenManager.hashToken(token);

        Instant now = Instant.now();
        Duration duration = Duration.ofMinutes(15);
        Instant futureInstant = now.plus(duration);

        user.setPasswordResetToken(hashedToken);
        user.setPasswordResetExpires(Timestamp.from(futureInstant));

        userRepository.save(user);
        return true;
    }

    public Optional<LoggedInUserDto> resetPassword(String resetToken, ResetPasswordRequestDto resetPasswordDto) {
        boolean passwordMatch = comparePasswords(resetPasswordDto.getPassword(), resetPasswordDto.getConfirmPassword());
        if (!passwordMatch) {
            throw new BadRequestException("Password must match with password confirm");
        }

        String hashedResetToken = TokenManager.hashToken(resetToken);
        Optional<User> optUser = userRepository.findByPasswordResetToken(hashedResetToken);
        if (optUser.isEmpty()) {
            throw new ResourceNotFoundException("User not found");
        }
        User user = optUser.get();

        if (user.getPasswordResetExpires().before(Timestamp.from(Instant.now()))) {
            throw new RuntimeException("Your reset password time is expired. Please try again");
        }

        user.setPassword(passwordEncoder.encode(resetPasswordDto.getPassword()));
        user.setPasswordChangedAt(Timestamp.from(Instant.now()));
        user.setPasswordResetToken(null);
        user.setPasswordResetExpires(null);
        User savedUser = userRepository.save(user);

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(savedUser.getUsername(), resetPasswordDto.getPassword()));

        String jwt = jwtService.generateToken(savedUser);
        LoggedInUserDto loggedInUserDto = new LoggedInUserDto(jwt, modelMapper.map(savedUser, UserDto.class));
        return Optional.of(loggedInUserDto);
    }

    public Optional<LoggedInUserDto> updatePassword(String id, UpdatePasswordRequestDto updatePasswordDto) {
        boolean passwordMatch = comparePasswords(updatePasswordDto.getNewPassword(), updatePasswordDto.getNewPasswordConfirm());
        if (!passwordMatch) {
            throw new BadRequestException("Password must match with password confirm");
        }

        UUID userId = TypeConverter.convertStringToUUID(id);
        Optional<User> optUser = userRepository.findById(userId);
        if (optUser.isEmpty()) {
            throw new ResourceNotFoundException("User not found");
        }
        User user = optUser.get();

        if (!passwordEncoder.matches(updatePasswordDto.getPassword(), user.getPassword())) {
            throw new BadRequestException("Entered password don't match old password");
        }

        user.setPassword(passwordEncoder.encode(updatePasswordDto.getNewPassword()));
        user.setPasswordChangedAt(Timestamp.from(Instant.now()));
        User savedUser = userRepository.save(user);

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(savedUser.getUsername(), updatePasswordDto.getNewPassword()));

        String jwt = jwtService.generateToken(savedUser);
        LoggedInUserDto loggedInUserDto = new LoggedInUserDto(jwt, modelMapper.map(savedUser, UserDto.class));
        return Optional.of(loggedInUserDto);
    }

    public Optional<LoggedInUserDto> verifyEmail(String token) {
        String hashedToken = TokenManager.hashToken(token);

        Optional<User> optUser = userRepository.findByConfirmEmailToken(hashedToken);
        if (optUser.isEmpty()) {
            throw new ResourceNotFoundException("User not found");
        }
        User user = optUser.get();
        if (user.getActiveStatus() != UserStatus.UNVERIFIED) {
            throw new BadRequestException("Only unverified user can be activated");
        }

        user.setActiveStatus(UserStatus.ACTIVE);
        user.setConfirmEmailToken(null);
        User savedUser = userRepository.save(user);

        String jwt = jwtService.generateToken(savedUser);
        LoggedInUserDto loggedInUserDto = new LoggedInUserDto(jwt, modelMapper.map(savedUser, UserDto.class));
        return Optional.of(loggedInUserDto);
    }

    public boolean sendVerificationToken(String id, String hostUrl) {
        UUID userId = TypeConverter.convertStringToUUID(id);
        Optional<User> optUser = userRepository.findById(userId);
        if (optUser.isEmpty())
            throw new ResourceNotFoundException("User not found");

        User user = optUser.get();
        if (user.getActiveStatus() != UserStatus.UNVERIFIED) {
            throw new BadRequestException("User status incorrect. Should be UNVERIFIED to activate");
        }

        String token = TokenManager.generateRandomToken();
        String hashedToken = TokenManager.hashToken(token);
        user.setConfirmEmailToken(hashedToken);
        userRepository.save(user);

        emailService.sendMessageAccountVerification(user.getEmail(), token, hostUrl);

        return true;
    }

    private boolean comparePasswords(String password, String passwordConfirm) {
        return password.equals(passwordConfirm);
    }
}
