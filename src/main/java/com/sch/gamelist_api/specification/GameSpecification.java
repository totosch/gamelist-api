package com.sch.gamelist_api.specification;

import com.sch.gamelist_api.model.Game;
import com.sch.gamelist_api.model.Genre;
import com.sch.gamelist_api.model.Platform;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

public class GameSpecification {

    public static Specification<Game> titleContains(String title) {
        return (root, query, criteriaBuilder) -> {
            if (title == null || title.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("title")),
                    "%" + title.toLowerCase() + "%"
            );
        };
    }

    public static Specification<Game> hasGenre(Long genreId) {
        return (root, query, criteriaBuilder) -> {
            if (genreId == null) {
                return criteriaBuilder.conjunction();
            }
            Join<Game, Genre> genres = root.join("genres");
            return criteriaBuilder.equal(genres.get("id"), genreId);
        };
    }

    public static Specification<Game> hasPlatform(Long platformId) {
        return (root, query, criteriaBuilder) -> {
            if (platformId == null) {
                return criteriaBuilder.conjunction();
            }
            Join<Game, Platform> platforms = root.join("platforms");
            return criteriaBuilder.equal(platforms.get("id"), platformId);
        };
    }
}