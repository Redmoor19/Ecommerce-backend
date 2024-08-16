package com.example.gameStore.repositories;

import com.example.gameStore.entities.Order;
import com.example.gameStore.enums.OrderStatus;
import com.example.gameStore.enums.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {

    List<Order> findAllByUserId(UUID userId);

    Optional<Order> findFirstByUserIdAndStatusAndPaymentStatus(UUID userId, OrderStatus status, PaymentStatus paymentStatus);

    List<Order> findAllByStatusAndPaymentStatus(OrderStatus status, PaymentStatus paymentStatus);

    List<Order> findAllByStatus(OrderStatus status);
}

