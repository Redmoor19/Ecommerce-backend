package com.example.gameStore.controllers;

import com.example.gameStore.dtos.GameDtos.GameDto;
import com.example.gameStore.dtos.GlobalResponse;
import com.example.gameStore.dtos.UserDtos.CreateUserRequestDto;
import com.example.gameStore.dtos.UserDtos.UpdateUserRequestDto;
import com.example.gameStore.dtos.UserDtos.UpdateUserRoleRequestDto;
import com.example.gameStore.dtos.UserDtos.UserDto;
import com.example.gameStore.services.interfaces.OrderService;
import com.example.gameStore.services.interfaces.UserService;
import com.example.gameStore.shared.exceptions.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/users")
@AllArgsConstructor
public class UserController {

    @Autowired
    private final UserService userService;
    @Autowired
    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<GlobalResponse<List<UserDto>>> findUsers() {
        List<UserDto> users = userService.getUsers();
        return ResponseEntity.ok(new GlobalResponse<>(users));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<GlobalResponse<UserDto>> findUserById(@PathVariable String userId) {
        Optional<UserDto> optUserDto = userService.getUserById(userId);
        return optUserDto
                .map(userDto -> ResponseEntity.ok(new GlobalResponse<>(userDto)))
                .orElseThrow(() -> new ResourceNotFoundException("User with such Id not found"));
    }

    @PostMapping
    public ResponseEntity<GlobalResponse<UserDto>> addUser(@RequestBody @Valid CreateUserRequestDto createUserDto) {
        Optional<UserDto> optCreatedUser = userService.createUser(createUserDto);
        orderService.createNewOrder(optCreatedUser);
        return optCreatedUser
                .map(userDto -> ResponseEntity.ok(new GlobalResponse<>(userDto)))
                .orElseThrow(() -> new RuntimeException("Something went wrong"));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<GlobalResponse<Void>> removeUser(@PathVariable String userId) {
        if (!userService.deleteUser(userId)) throw new ResourceNotFoundException("User with such Id not found");
        return ResponseEntity.ok(new GlobalResponse<>(null));
    }

    @PostMapping("/activate/{userId}")
    public ResponseEntity<GlobalResponse<Void>> activateUser(@PathVariable String userId) {
        if (!userService.activateUser(userId)) throw new ResourceNotFoundException("User with such Id not found");
        return ResponseEntity.ok(new GlobalResponse<>(null));
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<GlobalResponse<UserDto>> updateUser(@PathVariable String userId, @RequestBody @Valid UpdateUserRequestDto updateUserDto) {
        Optional<UserDto> optUpdatedUser = userService.updateUser(updateUserDto, userId);
        return optUpdatedUser
                .map(userDto -> ResponseEntity.ok(new GlobalResponse<>(userDto)))
                .orElseThrow(() -> new RuntimeException("Something went wrong"));
    }

    @PatchMapping("role/{userId}")
    public ResponseEntity<GlobalResponse<Void>> updateUserRole(@PathVariable String userId, @RequestBody @Valid UpdateUserRoleRequestDto roleDto) {
        if (!userService.updateUserRole(roleDto, userId))
            throw new ResourceNotFoundException("User with such Id not found");
        return ResponseEntity.ok(new GlobalResponse<>(null));
    }

    @GetMapping("/me")
    public ResponseEntity<GlobalResponse<UserDto>> findLoggedUser(HttpServletRequest request) {
        String userId = (String) request.getAttribute("userId");
        Optional<UserDto> optLoggedInUserDto = userService.getUserById(userId);
        return optLoggedInUserDto
                .map(userDto -> ResponseEntity.ok(new GlobalResponse<>(userDto)))
                .orElseThrow(() -> new ResourceNotFoundException("User with such Id not found"));
    }

    @DeleteMapping("/me")
    public ResponseEntity<GlobalResponse<Void>> removeLoggedInUser(HttpServletRequest request) {
        String userId = (String) request.getAttribute("userId");
        if (!userService.deleteUser(userId)) throw new ResourceNotFoundException("User with such Id not found");
        return ResponseEntity.ok(new GlobalResponse<>(null));
    }

    @PatchMapping("/me")
    public ResponseEntity<GlobalResponse<UserDto>> updateLoggedInUser(HttpServletRequest request, @RequestBody @Valid UpdateUserRequestDto updateUserDto) {
        String userId = (String) request.getAttribute("userId");
        Optional<UserDto> optUpdatedUser = userService.updateUser(updateUserDto, userId);
        return optUpdatedUser
                .map(userDto -> ResponseEntity.ok(new GlobalResponse<>(userDto)))
                .orElseThrow(() -> new RuntimeException("Something went wrong"));
    }

    @GetMapping("/me/games/favourites")
    public ResponseEntity<GlobalResponse<List<GameDto>>> findFavouriteGames(HttpServletRequest request) {
        String userId = (String) request.getAttribute("userId");
        List<GameDto> favouriteGames = userService.getFavouriteGames(userId);
        return ResponseEntity.ok(new GlobalResponse<>(favouriteGames));
    }

    @PostMapping("/me/games/favourites/{gameId}")
    public ResponseEntity<GlobalResponse<Void>> addUserFavourite(HttpServletRequest request, @PathVariable String gameId) {
        String userId = (String) request.getAttribute("userId");
        if (!userService.addFavouriteGame(userId, gameId)) throw new RuntimeException("Something went wrong");
        return ResponseEntity.ok(new GlobalResponse<>(null));
    }

    @DeleteMapping("/me/games/favourites/{gameId}")
    public ResponseEntity<GlobalResponse<Void>> deleteUserFavourite(HttpServletRequest request, @PathVariable String gameId) {
        String userId = (String) request.getAttribute("userId");
        if (!userService.removeFavouriteGame(userId, gameId)) throw new RuntimeException("Something went wrong");
        return ResponseEntity.ok(new GlobalResponse<>(null));
    }
}
