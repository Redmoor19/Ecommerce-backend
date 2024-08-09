package com.example.gameStore.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(
        name = "t_review",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "game_id"})
)
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = User.class)
    @JoinColumn(name = "user_id", nullable = false)
    private User userId;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Game.class)
    @JoinColumn(name = "game_id", nullable = false)
    private Game gameId;

    @Column(name = "description")
    private String description;

    @Column(name = "star_rating")
    private int starRating;

    @Column(name = "created_at", columnDefinition = "TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP")
    private Timestamp createdAt;

    public void setUserId(UUID userId) {
        if (userId != null) {
            this.userId = new User();
            this.userId.setId(userId);
        } else {
            this.userId = null;
        }
    }

    public UUID getUserId() {
        return this.userId.getId();
    }

    public void setGameId(UUID gameId) {
        if (gameId != null) {
            this.gameId = new Game();
            this.gameId.setId(gameId);
        } else {
            this.gameId = null;
        }
    }

    public UUID getGameId() {
        return this.gameId.getId();
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = Timestamp.from(Instant.now());
    }
}
