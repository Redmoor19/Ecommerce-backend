package com.example.gameStore.repositories;

import com.example.gameStore.entities.GameOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface GameOrderRepository extends JpaRepository<GameOrder, UUID> {
    //List<GameOrder> findAllByOrderId(UUID orderId);

    @Query("SELECT go FROM GameOrder go JOIN FETCH go.game WHERE go.order.id = :orderId")
    List<GameOrder> findAllByOrderId(@Param("orderId") UUID orderId);

}
