package com.example.gameStore.controllers;

import com.example.gameStore.dtos.GameDto;
import com.example.gameStore.dtos.OrderDto;
import com.example.gameStore.enums.OrderStatus;
import com.example.gameStore.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/")
@AllArgsConstructor
public class OrderController {

    @GetMapping("orders")
    public List<OrderDto> findAllOrders() {
        return List.of(new OrderDto(UUID.randomUUID(), UUID.randomUUID(), 100.0, new Timestamp(System.currentTimeMillis()), OrderStatus.DELIVERED, PaymentStatus.PAID, Collections.EMPTY_LIST), new OrderDto(UUID.randomUUID(), UUID.randomUUID(), 135.0, new Timestamp(System.currentTimeMillis()), OrderStatus.DELIVERED, PaymentStatus.PAID, Collections.EMPTY_LIST), new OrderDto(UUID.randomUUID(), UUID.randomUUID(), 135.0, new Timestamp(System.currentTimeMillis()), OrderStatus.PROCESSING, PaymentStatus.UNPAID, Collections.EMPTY_LIST));
    }

    @GetMapping("orders/{id}")
    public OrderDto findOrderById(@PathVariable(required = true, name = "id") String id) {
        return new OrderDto(UUID.fromString(id), UUID.randomUUID(), 100.0, new Timestamp(System.currentTimeMillis()), OrderStatus.DELIVERED, PaymentStatus.PAID, Collections.EMPTY_LIST);
    }

    @GetMapping("users/me/orders")
    public List<OrderDto> findCurrentUserOrders() {
        return List.of(new OrderDto(UUID.randomUUID(), UUID.randomUUID(), 100.0, new Timestamp(System.currentTimeMillis()), OrderStatus.DELIVERED, PaymentStatus.PAID, Collections.EMPTY_LIST), new OrderDto(UUID.randomUUID(), UUID.randomUUID(), 135.0, new Timestamp(System.currentTimeMillis()), OrderStatus.DELIVERED, PaymentStatus.PAID, Collections.EMPTY_LIST), new OrderDto(UUID.randomUUID(), UUID.randomUUID(), 135.0, new Timestamp(System.currentTimeMillis()), OrderStatus.PROCESSING, PaymentStatus.UNPAID, Collections.EMPTY_LIST));
    }

    @GetMapping("users/me/orders/{id}")
    public OrderDto findCurrentUserOrderById(@PathVariable(required = true, name = "id") String id) {
        return new OrderDto(UUID.fromString(id), UUID.randomUUID(), 100.0, new Timestamp(System.currentTimeMillis()), OrderStatus.DELIVERED, PaymentStatus.PAID, Collections.EMPTY_LIST);
    }

    @GetMapping("users/me/orders/current")
    public OrderDto findCurrentUserCurrentOrder() {
        return new OrderDto(UUID.randomUUID(), UUID.randomUUID(), 100.0, new Timestamp(System.currentTimeMillis()), OrderStatus.DELIVERED, PaymentStatus.PAID, Collections.EMPTY_LIST);
    }

    @PostMapping("users/me/orders/current/game/{id}")
    public OrderDto addGameToOrder(@PathVariable(required = true, name = "id") String id) {
        GameDto game = new GameDto(1, "Name", id);
        return new OrderDto(UUID.randomUUID(), UUID.randomUUID(), 100.0, new Timestamp(System.currentTimeMillis()), OrderStatus.DELIVERED, PaymentStatus.PAID, List.of(game));
    }

    @DeleteMapping("users/me/orders/current/game/{id}")
    public OrderDto deleteGameFromOrder(@PathVariable(required = true, name = "id") String id) {
        return new OrderDto(UUID.randomUUID(), UUID.randomUUID(), 100.0, new Timestamp(System.currentTimeMillis()), OrderStatus.DELIVERED, PaymentStatus.PAID, Collections.EMPTY_LIST);
    }

    @DeleteMapping("users/me/orders/current")
    public OrderDto cleanCurrentOrder() {
        return new OrderDto(UUID.randomUUID(), UUID.randomUUID(), 100.0, new Timestamp(System.currentTimeMillis()), OrderStatus.DELIVERED, PaymentStatus.PAID, Collections.EMPTY_LIST);
    }

    @PostMapping("users/me/orders/current/checkout")
    public OrderDto checkoutCurrentOrder() {
        GameDto game = new GameDto(1, "Name", "key");
        return new OrderDto(UUID.randomUUID(), UUID.randomUUID(), 100.0, new Timestamp(System.currentTimeMillis()), OrderStatus.PROCESSING, PaymentStatus.WAITING, List.of(game));
    }
}
