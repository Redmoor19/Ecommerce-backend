package com.example.gameStore.dtos.UserDtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UpdatePasswordRequestDto {
    private String password;
    private String newPassword;
    private String newPasswordConfirm;
}
