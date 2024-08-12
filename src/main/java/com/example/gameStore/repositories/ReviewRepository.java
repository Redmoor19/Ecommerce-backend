package com.example.gameStore.repositories;

import com.example.gameStore.dtos.ReviewDtos.EmbeddedReviewDto;
import com.example.gameStore.entities.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ReviewRepository extends JpaRepository<Review, UUID> {
    @Query("""
            SELECT new com.example.gameStore.dtos.ReviewDtos.EmbeddedReviewDto(
            r.id, r.description, r.starRating, r.createdAt, u.name
            )
            FROM Review r
            JOIN r.userId u
            JOIN r.gameId g
            WHERE g.id = :gameId
            """)
    List<EmbeddedReviewDto> findReviewsByGameId(@Param("gameId") UUID gameId);

}
