package com.example.gameStore.dtos.GameDtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MostPopularGamesDto {
    private UUID id;
    private String name;
    private String thumbnail;
    private Long totalQuantity;
}
