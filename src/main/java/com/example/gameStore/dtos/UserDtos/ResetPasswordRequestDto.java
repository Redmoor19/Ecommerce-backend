package com.example.gameStore.dtos.UserDtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ResetPasswordRequestDto {
    @NotNull
    @Size(min = 8, message = "Password should be at least 8 characters long")
    private String password;

    @NotNull
    @Size(min = 8, message = "Password confirm should be at least 8 characters long")
    private String confirmPassword;
}
