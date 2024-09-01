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
public class CreateUserRequestDto {
    @NotNull(message = "User must have the name")
    @Size(min = 1, message = "Name can't be less than 1 symbol")
    @Size(max = 30, message = "Input is too long for a name")
    private String name;

    @NotNull(message = "User must have the email")
    @Email(message = "Email not valid")
    private String email;

    @NotNull(message = "User must have the password")
    @Size(min = 8, message = "Password should be at least 8 characters long")
    private String password;

    @NotNull(message = "You have to confirm the password")
    @Size(min = 8, message = "Password confirm should be at least 8 characters long")
    private String confirmPassword;
}
