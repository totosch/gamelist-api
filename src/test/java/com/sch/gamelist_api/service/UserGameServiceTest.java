package com.sch.gamelist_api.service;

import com.sch.gamelist_api.dto.UserGameRequest;
import com.sch.gamelist_api.dto.UserGameResponse;
import com.sch.gamelist_api.exception.ResourceNotFoundException;
import com.sch.gamelist_api.model.Game;
import com.sch.gamelist_api.model.GameStatus;
import com.sch.gamelist_api.model.User;
import com.sch.gamelist_api.model.UserGame;
import com.sch.gamelist_api.repository.GameRepository;
import com.sch.gamelist_api.repository.UserGameRepository;
import com.sch.gamelist_api.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserGameServiceTest {

    @Mock
    private UserGameRepository userGameRepository;

    @Mock
    private GameRepository gameRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserGameService userGameService;

    @Test
    void addGame_whenGameAlreadyInList_throwsIllegalArgumentException() {
        UserGameRequest request = new UserGameRequest();
        request.setGameId(1L);
        request.setStatus(GameStatus.PLAYING);

        when(userGameRepository.existsByUserEmailAndGameId("test@test.com", 1L)).thenReturn(true);

        assertThatThrownBy(() -> userGameService.addGame("test@test.com", request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("already in list");
    }

    @Test
    void addGame_withValidData_returnsUserGameResponse() {
        UserGameRequest request = new UserGameRequest();
        request.setGameId(1L);
        request.setStatus(GameStatus.COMPLETED);
        request.setRating(10);

        User user = new User();
        user.setEmail("test@test.com");

        Game game = new Game();
        game.setId(1L);
        game.setTitle("Silksong");

        UserGame savedUserGame = new UserGame();
        savedUserGame.setId(1L);
        savedUserGame.setGame(game);
        savedUserGame.setStatus(GameStatus.COMPLETED);
        savedUserGame.setRating(10);

        when(userGameRepository.existsByUserEmailAndGameId("test@test.com", 1L)).thenReturn(false);
        when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.of(user));
        when(gameRepository.findById(1L)).thenReturn(Optional.of(game));
        when(userGameRepository.save(any(UserGame.class))).thenReturn(savedUserGame);

        UserGameResponse response = userGameService.addGame("test@test.com", request);

        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getGameTitle()).isEqualTo("Silksong");
        assertThat(response.getStatus()).isEqualTo(GameStatus.COMPLETED);
    }

    @Test
    void updateGame_whenGameNotInList_throwsResourceNotFoundException() {
        UserGameRequest request = new UserGameRequest();
        request.setStatus(GameStatus.COMPLETED);
        request.setRating(10);

        when(userGameRepository.findByUserEmailAndGameId("test@test.com", 1L))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> userGameService.updateGame("test@test.com", 1L, request))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Game not found in list");
    }

    @Test
    void removeGame_whenGameNotInList_throwsResourceNotFoundException() {
        UserGameRequest request = new UserGameRequest();
        request.setStatus(GameStatus.COMPLETED);
        request.setRating(10);

        when(userGameRepository.findByUserEmailAndGameId("test@test.com", 1L))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> userGameService.removeGame("test@test.com", 1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Game not found in list");
    }

    @Test
    void findByUserEmail_returnsListOfUserGameResponses() {
        Game game1 = new Game();
        game1.setTitle("Silksong");

        Game game2 = new Game();
        game2.setTitle("Hades 2");

        UserGame userGame1 = new UserGame();
        userGame1.setId(1L);
        userGame1.setGame(game1);
        userGame1.setStatus(GameStatus.PLAYING);

        UserGame userGame2 = new UserGame();
        userGame2.setId(2L);
        userGame2.setGame(game2);
        userGame2.setStatus(GameStatus.COMPLETED);

        when(userGameRepository.findByUserEmail("test@test.com"))
                .thenReturn(List.of(userGame1, userGame2));

        List<UserGameResponse> responses = userGameService.findByUserEmail("test@test.com");

        assertThat(responses).hasSize(2);
        assertThat(responses.get(0).getGameTitle()).isEqualTo("Silksong");
        assertThat(responses.get(1).getGameTitle()).isEqualTo("Hades 2");
    }

}
