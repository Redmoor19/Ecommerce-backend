package com.example.gameStore.services.interfaces;

import com.example.gameStore.dtos.OrderDtos.OrderDto;
import com.example.gameStore.dtos.OrderDtos.OrderWithUserDto;
import com.example.gameStore.dtos.UserDtos.UserDto;

import java.util.List;
import java.util.Optional;

public interface OrderService {
    List<OrderDto> findAllOrders();

    List<OrderWithUserDto> findAllExtendedOrders();

    Optional<OrderDto> findOrderById(String id);

    List<OrderDto> findOrdersByUser(String userId);

    Optional<OrderDto> findCurrentOrderByUser(String userId);

    boolean addGameToOrder(String gameId, String orderId);

    Optional<OrderDto> deleteGameFromOrder(String gameId, String orderId);

    Optional<OrderDto> cleanCurrentOrder(String orderId);

    Optional<OrderDto> checkoutCurrentOrder(String userId);

    Optional<OrderDto> createNewOrder(Optional<UserDto> userDto);
}
