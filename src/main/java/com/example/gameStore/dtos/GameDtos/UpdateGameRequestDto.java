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
public class UpdateGameRequestDto extends CreateGameRequestDto {
    private UUID id;
}
