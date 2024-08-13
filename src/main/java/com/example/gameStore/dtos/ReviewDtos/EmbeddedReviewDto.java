package com.example.gameStore.dtos.ReviewDtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EmbeddedReviewDto {
    private UUID reviewID;
    private String reviewDescription;
    private int starRating;
    private Timestamp createdAt;
    private String name;
}
