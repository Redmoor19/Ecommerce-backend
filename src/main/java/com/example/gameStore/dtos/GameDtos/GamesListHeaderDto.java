package com.example.gameStore.dtos.GameDtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GamesListHeaderDto {
    private int totalGamesCount;
    private int totalPages;
    private int gamesPerPage;
    private int currentPageNumber;
}
