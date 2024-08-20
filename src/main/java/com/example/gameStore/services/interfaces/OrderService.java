package com.example.gameStore.services.interfaces;

import com.example.gameStore.dtos.OrderDtos.OrderDto;
import com.example.gameStore.dtos.OrderDtos.OrderWithUserDto;
import com.example.gameStore.dtos.OrderDtos.PayDto;
import com.example.gameStore.dtos.UserDtos.UserDto;
import com.example.gameStore.entities.User;

import javax.naming.AuthenticationException;
import java.util.List;
import java.util.Optional;

public interface OrderService {
    List<OrderDto> findAllOrders();

    List<OrderWithUserDto> findAllExtendedOrders();

    Optional<OrderDto> findOrderById(String id);

    Optional<OrderDto> findOrderById(String orderId, String userId) throws AuthenticationException;

    List<OrderDto> findOrdersByUser(String userId);

    Optional<OrderDto> findCurrentOrderByUser(String userId);

    Optional<OrderDto> addGameToOrder(String gameId, String userId);

    Optional<OrderDto> deleteGameFromOrder(String gameId, String userId);

    Optional<OrderDto> cleanCurrentOrder(String userId);

    Optional<OrderDto> checkoutCurrentOrder(String userId);

    Optional<OrderDto> payForOrder(PayDto payDto, String userId);

    Optional<OrderDto> createNewOrder(Optional<UserDto> userDto);

    Optional<OrderDto> createNewOrder(User user);

    void declineAllOrdersIfDelay();
}
