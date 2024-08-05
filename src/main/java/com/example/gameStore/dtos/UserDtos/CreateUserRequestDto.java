package com.example.gameStore.dtos.UserDtos;

import com.example.gameStore.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CreateUserRequestDto {
    private String username;
    private String email;
    private String password;
    private String confirmPassword;
    private UserRole role;

    public boolean isValidEmail() {
        return email != null && email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    }

    public boolean isValidPassword() {
        return  password != null && password.length() > 7 && password.equals(confirmPassword);
    }
}
