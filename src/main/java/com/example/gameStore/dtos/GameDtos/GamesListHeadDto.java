package com.example.gameStore.dtos.GameDtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GamesListHeadDto {
    private int allGamesQuantity;
    private int pagesQuantity;
}
