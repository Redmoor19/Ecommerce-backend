package com.example.gameStore.dtos.ReviewDtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CreateOrUpdateReviewRequestDto {
    private String description;
    private int starRating;
}
