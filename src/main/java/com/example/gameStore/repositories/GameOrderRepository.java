package com.example.gameStore.repositories;

import com.example.gameStore.entities.GameOrder;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface GameOrderRepository extends JpaRepository<GameOrder, UUID> {

    @Query("SELECT go FROM GameOrder go JOIN FETCH go.game WHERE go.order.id = :orderId")
    List<GameOrder> findAllByOrderId(@Param("orderId") UUID orderId);

    Optional<GameOrder> findFirstByOrderIdAndGameId(UUID orderId, UUID gameId);

    @Modifying
    @Transactional
    @Query("DELETE FROM GameOrder go WHERE go.game.id = :gameId AND go.order.id = :orderId")
    void deleteByGameIdAndOrderId(@Param("gameId") UUID gameId, @Param("orderId") UUID orderId);

    @Modifying
    @Transactional
    @Query("DELETE FROM GameOrder go WHERE go.order.id = :orderId")
    void deleteByOrderId(@Param("orderId") UUID orderId);
}
