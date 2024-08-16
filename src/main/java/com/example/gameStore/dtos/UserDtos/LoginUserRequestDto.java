package com.example.gameStore.dtos.UserDtos;

import jakarta.validation.constraints.Email;
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
public class LoginUserRequestDto {
    @NotNull
    @Email
    private String email;

    @NotNull
    @Size(min = 8, message = "Password can't be less then 8 characters")
    private String password;
}
