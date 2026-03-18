package com.sch.gamelist_api.repository;

import com.sch.gamelist_api.model.UserGame;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserGameRepository extends JpaRepository<UserGame, Long> {

    List<UserGame> findByUserEmail(String email);

    Optional<UserGame> findByUserEmailAndGameId(String email, Long gameId);

    boolean existsByUserEmailAndGameId(String email, Long gameId);
}