package com.example.gameStore.entities;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class GameOrderId {
    private UUID orderId;
    private UUID gameId;
}
