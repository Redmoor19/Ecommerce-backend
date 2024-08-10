package com.example.gameStore.dtos.OrderDtos;

import com.example.gameStore.dtos.UserDtos.UserDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderWithUserDto {
    private String id;
    private UserDto user;
    private Double totalPrice;
    private String createdAt;
    private String status;
    private String paymentStatus;
    private List<GameOrderDto> games;
}
