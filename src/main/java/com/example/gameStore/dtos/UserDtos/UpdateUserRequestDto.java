package com.example.gameStore.dtos.UserDtos;

import com.example.gameStore.enums.UserRole;
import com.example.gameStore.enums.UserStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UpdateUserRequestDto {
    private String username;
    private String email;
    private UserRole role;
    private LocalDate birthDate;
    private UserStatus activeStatus;
    private String address;
    private String phone;
}
