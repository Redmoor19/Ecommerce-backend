package com.example.gameStore.services.interfaces;

import com.example.gameStore.dtos.OrderDtos.ExtendedOrderDto;
import com.example.gameStore.dtos.OrderDtos.OrderDto;
import com.example.gameStore.dtos.UserDtos.UserDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderService {
    List<OrderDto> findAllOrders();
    List<ExtendedOrderDto> findAllExtendedOrders();

    Optional<OrderDto> findOrderById(String id);

    List<OrderDto> findOrdersByUser(String userId);

    Optional<OrderDto> findCurrentOrderByUser(UUID userId);

    boolean addGameToOrder(String gameId, String orderId);

    Optional<OrderDto> deleteGameFromOrder(UUID gameId, UUID orderId);

    Optional<OrderDto> cleanCurrentOrder(UUID orderId);

    Optional<OrderDto> checkoutCurrentOrder();

    Optional<OrderDto> createNewOrder(Optional<UserDto> userDto);
}
