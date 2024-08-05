package com.example.gameStore.entities;

import com.example.gameStore.enums.UserRole;
import com.example.gameStore.enums.UserStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class User {
    private UUID id;
    private String username;
    private String email;
    private String password;
    private Timestamp createdAt;
    private UserRole role;
    private Date birthDate;
    private Timestamp passwordChangedAt;
    private String passwordResetToken;
    private Timestamp passwordResetExpires;
    private String confirmEmailToken;
    private UserStatus activeStatus;
    private String address;
    private String phone;
}
