package com.sch.gamelist_api.service;

import com.sch.gamelist_api.dto.UserGameRequest;
import com.sch.gamelist_api.dto.UserGameResponse;
import com.sch.gamelist_api.exception.ResourceNotFoundException;
import com.sch.gamelist_api.model.Game;
import com.sch.gamelist_api.model.User;
import com.sch.gamelist_api.model.UserGame;
import com.sch.gamelist_api.repository.GameRepository;
import com.sch.gamelist_api.repository.UserGameRepository;
import com.sch.gamelist_api.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserGameService {

    private final UserGameRepository userGameRepository;
    private final UserRepository userRepository;
    private final GameRepository gameRepository;

    public UserGameService(UserGameRepository userGameRepository,
                           UserRepository userRepository,
                           GameRepository gameRepository) {
        this.userGameRepository = userGameRepository;
        this.userRepository = userRepository;
        this.gameRepository = gameRepository;
    }

    public List<UserGameResponse> findByUserEmail(String email) {
        List<UserGame> userGames = userGameRepository.findByUserEmail(email);
        List<UserGameResponse> responses = new ArrayList<>();
        for (UserGame userGame : userGames) {
            responses.add(toResponse(userGame));
        }
        return responses;
    }

    public UserGameResponse addGame(String email, UserGameRequest request) {
        boolean alreadyExists = userGameRepository.existsByUserEmailAndGameId(email, request.getGameId());
        if (alreadyExists) {
            throw new IllegalArgumentException("Game already in list");
        }

        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new ResourceNotFoundException("User not found with email: " + email)
        );

        Game game = gameRepository.findById(request.getGameId()).orElseThrow(
                () -> new ResourceNotFoundException("Game not found with id: " + request.getGameId())
        );

        UserGame userGame = new UserGame();
        userGame.setUser(user);
        userGame.setGame(game);
        userGame.setStatus(request.getStatus());
        userGame.setRating(request.getRating());

        UserGame savedUserGame = userGameRepository.save(userGame);
        return toResponse(savedUserGame);
    }

    public UserGameResponse updateGame(String email, Long gameId, UserGameRequest request) {
        UserGame userGame = userGameRepository.findByUserEmailAndGameId(email, gameId).orElseThrow(
                () -> new ResourceNotFoundException("Game not found in list")
        );

        userGame.setStatus(request.getStatus());
        userGame.setRating(request.getRating());

        UserGame updatedUserGame = userGameRepository.save(userGame);
        return toResponse(updatedUserGame);
    }

    public void removeGame(String email, Long gameId) {
        UserGame userGame = userGameRepository.findByUserEmailAndGameId(email, gameId).orElseThrow(
                () -> new ResourceNotFoundException("Game not found in list")
        );
        userGameRepository.deleteById(userGame.getId());
    }

    private UserGameResponse toResponse(UserGame userGame) {
        UserGameResponse response = new UserGameResponse();
        response.setId(userGame.getId());
        response.setGameTitle(userGame.getGame().getTitle());
        response.setStatus(userGame.getStatus());
        response.setRating(userGame.getRating());
        return response;
    }
}