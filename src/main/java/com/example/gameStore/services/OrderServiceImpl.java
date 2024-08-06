package com.example.gameStore.services;

import com.example.gameStore.dtos.OrderDto;
import com.example.gameStore.enums.OrderStatus;
import com.example.gameStore.enums.PaymentStatus;
import com.example.gameStore.services.interfaces.OrderService;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {

    public List<OrderDto> findAllOrders() {
        return List.of(new OrderDto(UUID.randomUUID(), UUID.randomUUID(), 100.0, new Timestamp(System.currentTimeMillis()),
                        OrderStatus.DELIVERED, PaymentStatus.PAID, Collections.EMPTY_LIST),
                new OrderDto(UUID.randomUUID(), UUID.randomUUID(), 135.0, new Timestamp(System.currentTimeMillis()),
                        OrderStatus.DELIVERED, PaymentStatus.PAID, Collections.EMPTY_LIST),
                new OrderDto(UUID.randomUUID(), UUID.randomUUID(), 135.0, new Timestamp(System.currentTimeMillis()),
                        OrderStatus.PROCESSING, PaymentStatus.UNPAID, Collections.EMPTY_LIST));
    }

    public Optional<OrderDto> findOrderById(String id) {
        return Optional.of(new OrderDto(UUID.fromString(id), UUID.randomUUID(), 100.0, new Timestamp(System.currentTimeMillis()),
                OrderStatus.DELIVERED, PaymentStatus.PAID, Collections.EMPTY_LIST));
    }

    public List<OrderDto> findOrdersByUser(UUID userId) {
        return List.of(new OrderDto(UUID.randomUUID(), UUID.randomUUID(), 100.0, new Timestamp(System.currentTimeMillis()),
                        OrderStatus.DELIVERED, PaymentStatus.PAID, Collections.EMPTY_LIST),
                new OrderDto(UUID.randomUUID(), UUID.randomUUID(), 135.0, new Timestamp(System.currentTimeMillis()),
                        OrderStatus.DELIVERED, PaymentStatus.PAID, Collections.EMPTY_LIST),
                new OrderDto(UUID.randomUUID(), UUID.randomUUID(), 135.0, new Timestamp(System.currentTimeMillis()),
                        OrderStatus.PROCESSING, PaymentStatus.UNPAID, Collections.EMPTY_LIST));
    }

    public Optional<OrderDto> findCurrentOrderByUser(UUID userId) {
        return Optional.of(new OrderDto(UUID.randomUUID(), UUID.randomUUID(), 100.0, new Timestamp(System.currentTimeMillis()),
                OrderStatus.DELIVERED, PaymentStatus.PAID, Collections.EMPTY_LIST));
    }

    public boolean addGameToOrder(UUID gameId, UUID orderId) {
        return true;
    }

    public Optional<OrderDto> deleteGameFromOrder(UUID gameId, UUID orderId) {
        return Optional.of(new OrderDto(UUID.randomUUID(), UUID.randomUUID(), 100.0, new Timestamp(System.currentTimeMillis()),
                OrderStatus.DELIVERED, PaymentStatus.PAID, Collections.EMPTY_LIST));
    }

    public Optional<OrderDto> cleanCurrentOrder(UUID orderId) {
        return Optional.of(new OrderDto(UUID.randomUUID(), UUID.randomUUID(), 100.0, new Timestamp(System.currentTimeMillis()),
                OrderStatus.DELIVERED, PaymentStatus.PAID, Collections.EMPTY_LIST));
    }

    public Optional<OrderDto> checkoutCurrentOrder() {
        return createNewOrder();
    }

    private Optional<OrderDto> createNewOrder() {
        return Optional.of(new OrderDto(UUID.randomUUID(), UUID.randomUUID(), 100.0, new Timestamp(System.currentTimeMillis()),
                OrderStatus.DELIVERED, PaymentStatus.PAID, Collections.EMPTY_LIST));
    }
}
