package com.example.gameStore.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDto {
    private UUID id;
    private UUID userId;
    private UUID productId;
    private String description;
    private int starRating;
    private Date createdAt;
}
