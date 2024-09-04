package com.example.gameStore.repositories;

import com.example.gameStore.dtos.GameDtos.MostPopularGamesDto;
import com.example.gameStore.entities.GameOrder;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
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


    @Query(value = "select sum(total_price) from \"order\" where payment_status = 'PAID' AND updated_at > NOW() - INTERVAL '1 month';", nativeQuery = true)
    Double getTotalPriceLastMonth();

    @Query(value = "SELECT new com.example.gameStore.dtos.GameDtos.MostPopularGamesDto(g.id, g.name, g.thumbnail, SUM(go.quantity)) " +
            "FROM Order o " +
            "JOIN GameOrder go ON o.id = go.order.id " +
            "JOIN Game g ON g.id = go.game.id " +
            "WHERE o.paymentStatus = 'PAID' AND o.updatedAt > :thirtyDaysAgoTimestamp AND g.isActive = true " +
            "GROUP BY g.id")
    List<MostPopularGamesDto> getMostPurchasedLastMonth(@Param("thirtyDaysAgoTimestamp") Timestamp thirtyDaysAgoTimestamp);
}
