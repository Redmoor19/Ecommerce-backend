package com.example.gameStore.dtos.UserDtos;

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
    private String name;
    private String email;
    private LocalDate birthDate;
    private String address;
    private String phone;
}
