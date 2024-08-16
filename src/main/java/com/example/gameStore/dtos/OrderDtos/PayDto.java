package com.example.gameStore.dtos.OrderDtos;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PayDto {
    @NotNull
    private String orderId;
    @NotNull
    private Boolean isPaidSuccessfully;
}
