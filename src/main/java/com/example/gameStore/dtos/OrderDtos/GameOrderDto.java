package com.example.gameStore.dtos.OrderDtos;

import com.example.gameStore.dtos.GameDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GameOrderDto {
    private GameDto game;
    private Integer quantity;
}
