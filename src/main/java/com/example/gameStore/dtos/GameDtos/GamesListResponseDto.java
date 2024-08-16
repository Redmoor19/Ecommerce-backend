package com.example.gameStore.dtos.GameDtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GamesListResponseDto {
    private GamesListHeaderDto allGamesHead;
    private List<GameDto> allGamesList;

    public boolean isEmpty() {
        return allGamesList == null || allGamesList.isEmpty();
    }
}
