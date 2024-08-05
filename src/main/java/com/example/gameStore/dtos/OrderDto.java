package com.example.gameStore.dtos;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {
    private UUID uuid = UUID.randomUUID();
    private UUID userUuid = UUID.randomUUID();
    private Double totalPrice;
    private Double deliveryPrice;
    private Date deliveryDate;

}
