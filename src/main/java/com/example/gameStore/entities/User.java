package com.example.gameStore.entities;

import com.example.gameStore.enums.UserRole;
import com.example.gameStore.enums.UserStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "\"user\"")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name", unique = true, nullable = false, length = 30)
    private String name;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, columnDefinition = "VARCHAR(255) DEFAULT 'USER'")
    private UserRole role;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "password_changed_at", columnDefinition = "TIMESTAMPTZ")
    private Timestamp passwordChangedAt;

    @Column(name = "password_reset_token", unique = true)
    private String passwordResetToken;

    @Column(name = "password_reset_expires", columnDefinition = "TIMESTAMPTZ")
    private Timestamp passwordResetExpires;

    @Column(name = "confirm_email_token", unique = true)
    private String confirmEmailToken;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", columnDefinition = "VARCHAR(255) DEFAULT 'UNVERIFIED'")
    private UserStatus activeStatus;

    @Column(name = "address")
    private String address;

    @Column(name = "phone")
    private String phone;

    @Column(name = "created_at", columnDefinition = "TIMESTAMPTZ")
    private Timestamp createdAt;

    @PrePersist
    public void onCreate() {
        if (this.role == null) {
            this.role = UserRole.USER;
        }
        if (this.activeStatus == null) {
            this.activeStatus = UserStatus.UNVERIFIED;
        }
        this.createdAt = Timestamp.from(Instant.now());
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    public String getUsername() {
        return email;
    }

    public boolean isAccountNonLocked() {
        return activeStatus != UserStatus.NOT_ACTIVE;
    }
}
