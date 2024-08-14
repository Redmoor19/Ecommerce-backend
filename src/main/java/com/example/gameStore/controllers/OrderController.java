package com.example.gameStore.controllers;

import com.example.gameStore.dtos.GlobalResponse;
import com.example.gameStore.dtos.OrderDtos.OrderDto;
import com.example.gameStore.dtos.OrderDtos.OrderWithUserDto;
import com.example.gameStore.services.interfaces.OrderService;
import com.example.gameStore.shared.exceptions.BadRequestException;
import com.example.gameStore.shared.exceptions.NoContentException;
import com.example.gameStore.shared.exceptions.ResourceNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/")
@AllArgsConstructor
public class OrderController {

    @Autowired
    private final OrderService orderService;

    @GetMapping("orders")
    public ResponseEntity<GlobalResponse<List<OrderDto>>> findAllOrders() {
        List<OrderDto> orders = orderService.findAllOrders();
        if (orders.isEmpty()) {
            throw new ResourceNotFoundException("No orders found");
        }
        return ResponseEntity.ok(new GlobalResponse<>(orders));
    }

    @GetMapping("extended-orders")
    public ResponseEntity<List<OrderWithUserDto>> findAllExtendedOrders() {
        List<OrderWithUserDto> orders = orderService.findAllExtendedOrders();
        if (orders.isEmpty()) {
            throw new ResourceNotFoundException("No orders found");
        }
        return ResponseEntity.ok(orders);
    }

    @GetMapping("orders/{id}")
    public ResponseEntity<GlobalResponse<OrderDto>> findOrderById(@PathVariable(required = true, name = "id") String id) {
        Optional<OrderDto> order = orderService.findOrderById(id);
        return order.map(o -> ResponseEntity.ok(new GlobalResponse<>(o)))
                .orElseThrow(() -> new ResourceNotFoundException("No orders found by id: " + id));
    }

    @GetMapping("users/me/orders")
    public ResponseEntity<List<OrderDto>> findCurrentUserOrders() {
        List<OrderDto> orders = orderService.findOrdersByUser(UUID.randomUUID().toString());
        if (orders.isEmpty()) {
            throw new NoContentException("No order found");
        }
        return ResponseEntity.ok(orders);
    }

    @GetMapping("users/me/orders/{id}")
    public ResponseEntity<OrderDto> findCurrentUserOrderById(@PathVariable(required = true, name = "id") String id) {
        Optional<OrderDto> order = orderService.findOrderById(id);
        return order.map(ResponseEntity::ok).orElseThrow(() -> new ResourceNotFoundException("No order found by id: " + id));
    }

    @GetMapping("users/me/orders/current/{user_id}")
    public ResponseEntity<OrderDto> findCurrentUserCurrentOrder(@PathVariable(required = true, name = "user_id") String id) {
        Optional<OrderDto> order = orderService.findCurrentOrderByUser(id);
        return order.map(ResponseEntity::ok).orElseThrow(() -> new ResourceNotFoundException("No order found"));
    }

    @GetMapping("/orders/user/{user_id}")
    public ResponseEntity<List<OrderDto>> findOrderByUserId(@PathVariable(required = true, name = "user_id") String id) {
        List<OrderDto> orders = orderService.findOrdersByUser(id);
        if (orders.isEmpty()) {
            throw new ResourceNotFoundException("No order found by user id: " + id);
        }
        return ResponseEntity.ok(orders);
    }

    @PostMapping("users/me/orders/current/game/{id}/{order_id}")
    public ResponseEntity<Void> addGameToOrder(@PathVariable(required = true, name = "id") String id,
                                               @PathVariable(required = true, name = "order_id") String orderId) {
        if (!orderService.addGameToOrder(id, orderId)) {
            throw new BadRequestException("Something went wrong");
        }
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("users/me/orders/current/game/{id}/{order_id}")
    public ResponseEntity<OrderDto> deleteGameFromOrder(@PathVariable(required = true, name = "id") String id,
                                                        @PathVariable(required = true, name = "order_id") String orderId) {
        return orderService.deleteGameFromOrder(id, orderId)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new BadRequestException("Something went wrong"));
    }

    @DeleteMapping("users/me/orders/current/{order_id}")
    public ResponseEntity<OrderDto> cleanCurrentOrder(@PathVariable(required = true, name = "order_id") String orderId) {
        return orderService.cleanCurrentOrder(orderId)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new BadRequestException("Something went wrong"));
    }

    @PostMapping("users/me/orders/current/checkout/{user_id}")
    public ResponseEntity<OrderDto> checkoutCurrentOrder(@PathVariable(required = true, name = "user_id") String user_id) {
        return orderService.checkoutCurrentOrder(user_id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new BadRequestException("Something went wrong"));
    }
}
