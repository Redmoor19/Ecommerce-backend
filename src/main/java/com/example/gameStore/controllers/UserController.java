package com.example.gameStore.controllers;

import com.example.gameStore.dtos.GameDtos.GameDto;
import com.example.gameStore.dtos.OrderDtos.OrderDto;
import com.example.gameStore.dtos.UserDtos.CreateUserRequestDto;
import com.example.gameStore.dtos.UserDtos.UpdateUserRequestDto;
import com.example.gameStore.dtos.UserDtos.UpdateUserRoleRequestDto;
import com.example.gameStore.dtos.UserDtos.UserDto;
import com.example.gameStore.services.interfaces.OrderService;
import com.example.gameStore.services.interfaces.UserService;
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
import java.util.UUID;

@RestController
@RequestMapping("api/v1/users")
@AllArgsConstructor
public class UserController {

    @Autowired
    private final UserService userService;
    @Autowired
    private final OrderService orderService;

    @GetMapping
    public List<UserDto> findUsers() {
        return userService.getUsers();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> findUserById(@PathVariable String userId) {
        Optional<UserDto> optUserDto = userService.getUserById(UUID.fromString(userId));
        return optUserDto.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<UserDto> addUser(@RequestBody CreateUserRequestDto createUserDto) {
        Optional<UserDto> optCreatedUser = userService.createUser(createUserDto);
        Optional<OrderDto> order = orderService.createNewOrder(optCreatedUser);
        return optCreatedUser.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> removeUser(@PathVariable String userId) {
        if (userService.deleteUser(userId)) {
            return ResponseEntity.status(204).build();
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/activate/{userId}")
    public ResponseEntity<Void> activateUser(@PathVariable String userId) {
        if (userService.activateUser(userId)) {
            return ResponseEntity.status(200).build();
        }
        return ResponseEntity.notFound().build();
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<UserDto> updateUser(@PathVariable String userId, @RequestBody UpdateUserRequestDto updateUserDto) {
        Optional<UserDto> optUpdatedUser = userService.updateUser(updateUserDto, userId);
        return optUpdatedUser.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @PatchMapping("role/{userId}")
    public ResponseEntity<Void> updateUserRole(@PathVariable String userId, @RequestBody UpdateUserRoleRequestDto roleDto) {
        boolean isUpdated = userService.updateUserRole(roleDto, userId);
        return isUpdated ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> findLoggedUser() {
        Optional<UserDto> optLoggedInUserDto = userService.getCurrentUser();
        return optLoggedInUserDto.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @DeleteMapping("/delete-me/{userId}")
    public ResponseEntity<Void> removeLoggedInUser(@PathVariable String userId) {
        if (userService.deleteUser(userId)) {
            return ResponseEntity.status(204).build();
        }
        return ResponseEntity.notFound().build();
    }

    @PatchMapping("/update-me")
    public ResponseEntity<UserDto> updateLoggedInUser(@RequestBody UpdateUserRequestDto updateUserDto) {
        String userId = "sdsfa";
        Optional<UserDto> updatedUser = userService.updateUser(updateUserDto, userId);
        return updatedUser.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @GetMapping("/me/games/favourites/{userId}")
    public ResponseEntity<List<GameDto>> findFavouriteGames(@PathVariable String userId) {
        Optional<List<GameDto>> favouriteGames = userService.getFavouriteGames(userId);
        return favouriteGames.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/me/games/favourites/{gameId}/{userId}")
    public ResponseEntity<Void> addUserFavourite(@PathVariable String gameId, @PathVariable String userId) {
        boolean isAdded = userService.addFavouriteGame(userId, gameId);
        return isAdded ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/me/games/favourites/{gameId}/{userId}")
    public ResponseEntity<Void> deleteUserFavourite(@PathVariable String gameId, @PathVariable String userId) {
        boolean isDeleted = userService.removeFavouriteGame(userId, gameId);
        return isDeleted ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
}
