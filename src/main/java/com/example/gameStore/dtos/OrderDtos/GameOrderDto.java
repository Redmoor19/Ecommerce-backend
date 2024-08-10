package com.example.gameStore.dtos.OrderDtos;

import com.example.gameStore.dtos.GameDtos.GameDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GameOrderDto {
    private GameDto game;
    private Integer quantity;
}
