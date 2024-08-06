package com.example.gameStore.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class KeyDto {
    private UUID id;
    private UUID value;
    private Date createdAt;
    private UUID gameId;
}
