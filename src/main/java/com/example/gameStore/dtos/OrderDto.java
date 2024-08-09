package com.example.gameStore.dtos;


import com.example.gameStore.dtos.GameDtos.GameDto;
import com.example.gameStore.enums.OrderStatus;
import com.example.gameStore.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {
    private UUID uuid;
    private UUID userUuid;
    private Double totalPrice;
    private Timestamp createdAt;
    private OrderStatus status;
    private PaymentStatus paymentStatus;
    private List<GameDto> games;
}
