package com.example.gameStore.controllers;

import com.example.gameStore.dtos.UserDtos.CreateUserRequestDto;
import com.example.gameStore.dtos.UserDtos.UpdateUserRequestDto;
import com.example.gameStore.dtos.UserDtos.UserDto;
import com.example.gameStore.services.UserServiceImpl;
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
    private UserServiceImpl userService;

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
        return optCreatedUser.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{userId}")
    public boolean removeUser(@PathVariable String userId) {
        return userService.deleteUser(UUID.fromString(userId));
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<UserDto> updateUser(@PathVariable String userId, @RequestBody UpdateUserRequestDto updateUserDto) {
        boolean matchId = updateUserDto.getId().equals(UUID.fromString(userId));
        if (!matchId) return ResponseEntity.badRequest().build();
        Optional<UserDto> optUpdatedUser = userService.updateUser(updateUserDto);
        return optUpdatedUser.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @GetMapping("/users/me")
    public ResponseEntity<UserDto> findLoggedUser() {
        Optional<UserDto> optLoggedInUserDto = userService.getCurrentUser();
        return optLoggedInUserDto.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @DeleteMapping("/delete-me")
    public ResponseEntity<Void> removeLoggedInUser() {
        UUID userId = UUID.randomUUID();
        if (userService.deleteUser(userId)) {
            return ResponseEntity.ok().build();

        }
        return ResponseEntity.badRequest().build();
    }

    @PatchMapping("/update-me")
    public ResponseEntity<UserDto> updateLoggedInUser(@RequestBody UpdateUserRequestDto updateUserDto) {
        Optional<UserDto> updatedUser = userService.updateUser(updateUserDto);
        return updatedUser.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.badRequest().build());
    }
}
