package com.example.gameStore.dtos.KeyDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class KeyCreationDto {
    private UUID value;
    private UUID gameId;
}
