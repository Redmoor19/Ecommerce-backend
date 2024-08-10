package com.example.gameStore.dtos.GameDtos;

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
public class SingleGameWithReviewsQueryDto {
    private UUID id;
    private String name;
    private int quantity;
    private String thumbnail;
    private List<String> images;
    private String developer;
    private Timestamp releaseDate;
    private String systemRequirements;
    private float price;
    private String description;
    private String sku;
    private float averageRating;
    private boolean isActive;
    private List<String> genreList;
    private List<String> playerSupport;
    private UUID reviewID;
    private String reviewDescription;
    private int starRating;
    private Timestamp createdAt;
    private String username;
}
