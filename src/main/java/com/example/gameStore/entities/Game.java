package com.example.gameStore.entities;

import com.example.gameStore.enums.Genre;
import com.example.gameStore.enums.PlayerSupport;
import com.example.gameStore.utilities.GameUtilities;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "game")
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name", unique = true, nullable = false, columnDefinition = "VARCHAR(50)")
    private String name;

    @ElementCollection(targetClass = Genre.class)
    @JoinTable(name = "genre", joinColumns = @JoinColumn(name = "genre_value"))
    @Enumerated(EnumType.STRING)
    @Column(name = "genre")
    private List<Genre> genreList;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "thumbnail", nullable = false)
    private String thumbnail = "https://res.cloudinary.com/df5iprard/image/upload/v1723707873/2024-08-15_104400_j19p5p.png";

    @Column(name = "image", columnDefinition = "TEXT[]")
    private List<String> images;

    @Column(name = "developer", nullable = false)
    private String developer;

    @Column(name = "release_date", nullable = false, columnDefinition = "TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP")
    private Timestamp releaseDate;

    @Column(name = "system_requirements", nullable = false)
    private String systemRequirements;

    @ElementCollection(targetClass = PlayerSupport.class)
    @JoinTable(name = "player_support", joinColumns = @JoinColumn(name = "player_support_value"))
    @Enumerated(EnumType.STRING)
    @Column(name = "player_support")
    private List<PlayerSupport> playerSupport;

    @Column(name = "price", columnDefinition = "NUMERIC(10, 2)")
    private float price;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "sku", nullable = false, unique = true)
    private String sku;

    @Column(name = "is_active")
    private boolean isActive;

    @Column(name = "average_rating", nullable = false, columnDefinition = "NUMERIC(2, 1) DEFAULT 0")
    private float averageRating;

    @PrePersist
    protected void onCreate() {
        this.sku = GameUtilities.generateSku();
    }

}
