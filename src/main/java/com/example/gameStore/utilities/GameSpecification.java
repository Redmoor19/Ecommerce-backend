package com.example.gameStore.utilities;

import com.example.gameStore.entities.Game;
import com.example.gameStore.enums.Genre;
import com.example.gameStore.enums.PlayerSupport;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class GameSpecification {

    public static Specification<Game> isActive() {
        return (Root<Game> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            return cb.isTrue(root.get("isActive"));
        };
    }

    public static Specification<Game> withFilters(String name, List<Genre> genres, List<PlayerSupport> supports) {
        return (Root<Game> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            Predicate predicate = cb.conjunction();

            if (name != null && !name.isEmpty()) {
                System.out.println("=============================================================Filtering by name: " + name);
                predicate = cb.and(predicate, cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
            }

            if (genres != null && !genres.isEmpty()) {
                System.out.println("===========================================================Filtering by genres: " + genres);
                Predicate genrePredicate = cb.disjunction();
                for (Genre genre : genres) {
                    genrePredicate = cb.or(genrePredicate, cb.isMember(genre, root.get("genreList")));
                }
                predicate = cb.and(predicate, genrePredicate);
            }

            if (supports != null && !supports.isEmpty()) {
                System.out.println("========================================================Filtering by supports: " + supports);
                Predicate supportPredicate = cb.disjunction();
                for (PlayerSupport support : supports) {
                    supportPredicate = cb.or(supportPredicate, cb.isMember(support, root.get("playerSupport")));
                }
                predicate = cb.and(predicate, supportPredicate);
            }

            return predicate;
        };
    }
}