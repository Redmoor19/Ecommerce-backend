package com.example.gameStore.dtos.OrderDtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AllOrdersDto {
    private List<OrderDto> orders;
    private Map<String, Integer> stats;
    private Double totalSum;
}
