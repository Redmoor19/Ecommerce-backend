package com.example.gameStore.dtos.ReviewDtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDto {
    private UUID id;
    private UUID userId;
    private UUID gameId;
    private String description;
    private int starRating;
    private Timestamp createdAt;
}
