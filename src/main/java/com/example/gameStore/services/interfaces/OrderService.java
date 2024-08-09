package com.example.gameStore.services.interfaces;

import com.example.gameStore.dtos.OrderDto;
import com.example.gameStore.dtos.UserDtos.UserDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderService {
    List<OrderDto> findAllOrders();

    Optional<OrderDto> findOrderById(String id);

    List<OrderDto> findOrdersByUser(String userId);

    Optional<OrderDto> findCurrentOrderByUser(UUID userId);

    boolean addGameToOrder(UUID gameId, UUID orderId);

    Optional<OrderDto> deleteGameFromOrder(UUID gameId, UUID orderId);

    Optional<OrderDto> cleanCurrentOrder(UUID orderId);

    Optional<OrderDto> checkoutCurrentOrder();

    Optional<OrderDto> createNewOrder(Optional<UserDto> userDto);
}
