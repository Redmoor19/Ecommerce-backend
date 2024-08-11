package com.example.gameStore.dtos.ReviewDtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UpdateReviewRequestDto extends CreateReviewRequestDto {
    private UUID id;
}
