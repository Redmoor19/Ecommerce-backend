package com.example.gameStore.controllers;

import com.example.gameStore.dtos.GlobalResponse;
import com.example.gameStore.dtos.OrderDtos.OrderDto;
import com.example.gameStore.dtos.OrderDtos.OrderWithUserDto;
import com.example.gameStore.services.interfaces.OrderService;
import com.example.gameStore.shared.exceptions.BadRequestException;
import com.example.gameStore.shared.exceptions.NoContentException;
import com.example.gameStore.shared.exceptions.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.naming.AuthenticationException;
import java.util.List;
import java.util.Optional;

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
    public ResponseEntity<List<OrderDto>> findOrdersOfCurrentUser(HttpServletRequest request) {
        String userId = (String) request.getAttribute("userId");
        List<OrderDto> orders = orderService.findOrdersByUser(userId);
        if (orders.isEmpty()) {
            throw new NoContentException("No order found");
        }
        return ResponseEntity.ok(orders);
    }

    @GetMapping("users/me/orders/{id}")
    public ResponseEntity<OrderDto> findOrderOfCurrentUserById(HttpServletRequest request,
                                                               @PathVariable(required = true, name = "id") String id) throws AuthenticationException {
        String userId = (String) request.getAttribute("userId");
        Optional<OrderDto> order = orderService.findOrderById(id, userId);
        return order.map(ResponseEntity::ok).orElseThrow(() -> new ResourceNotFoundException("No order found by id: " + id));
    }

    @GetMapping("users/me/orders/current")
    public ResponseEntity<OrderDto> findCurrentUserCurrentOrder(HttpServletRequest request) {
        String userId = (String) request.getAttribute("userId");
        Optional<OrderDto> order = orderService.findCurrentOrderByUser(userId);
        return order.map(ResponseEntity::ok).orElseThrow(() -> new ResourceNotFoundException("No order found"));
    }

    @GetMapping("/orders/user")
    public ResponseEntity<List<OrderDto>> findOrderByUserId(HttpServletRequest request) {
        String userId = (String) request.getAttribute("userId");
        List<OrderDto> orders = orderService.findOrdersByUser(userId);
        if (orders.isEmpty()) {
            throw new ResourceNotFoundException("No order found by user id: " + userId);
        }
        return ResponseEntity.ok(orders);
    }

    @PostMapping("users/me/orders/current/game/{id}")
    public ResponseEntity<Void> addGameToOrder(HttpServletRequest request,
                                               @PathVariable(required = true, name = "id") String id) {
        String userId = (String) request.getAttribute("userId");
        if (!orderService.addGameToOrder(id, userId)) {
            throw new BadRequestException("Something went wrong");
        }
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("users/me/orders/current/game/{id}")
    public ResponseEntity<OrderDto> deleteGameFromOrder(HttpServletRequest request,
                                                        @PathVariable(required = true, name = "id") String id) {
        String userId = (String) request.getAttribute("userId");
        return orderService.deleteGameFromOrder(id, userId)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new BadRequestException("Something went wrong"));
    }

    @DeleteMapping("users/me/orders/current")
    public ResponseEntity<OrderDto> cleanCurrentOrder(HttpServletRequest request) {
        String userId = (String) request.getAttribute("userId");
        return orderService.cleanCurrentOrder(userId)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new BadRequestException("Something went wrong"));
    }

    @PostMapping("users/me/orders/current/checkout")
    public ResponseEntity<OrderDto> checkoutCurrentOrder(HttpServletRequest request) {
        String userId = (String) request.getAttribute("userId");
        return orderService.checkoutCurrentOrder(userId)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new BadRequestException("Something went wrong"));
    }

    @PostMapping("users/me/orders/current/pay")
    public ResponseEntity<OrderDto> payCurrentOrder(HttpServletRequest request) {
        String userId = (String) request.getAttribute("userId");
        return orderService.payForOrder(userId)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new BadRequestException("Something went wrong"));
    }
}
