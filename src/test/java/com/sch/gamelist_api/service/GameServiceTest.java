package com.sch.gamelist_api.service;

import com.sch.gamelist_api.dto.GameRequest;
import com.sch.gamelist_api.dto.GameResponse;
import com.sch.gamelist_api.exception.ResourceNotFoundException;
import com.sch.gamelist_api.model.Game;
import com.sch.gamelist_api.model.Genre;
import com.sch.gamelist_api.model.Platform;
import com.sch.gamelist_api.repository.GameRepository;
import com.sch.gamelist_api.repository.GenreRepository;
import com.sch.gamelist_api.repository.PlatformRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GameServiceTest {

    @Mock
    private GameRepository gameRepository;

    @Mock
    private GenreRepository genreRepository;

    @Mock
    private PlatformRepository platformRepository;

    @InjectMocks
    private GameService gameService;


    @Test
    void findById_whenGameExists_returnsGameResponse() {
        Game game = new Game();
        game.setId(1L);
        game.setTitle("Silksong");

        when(gameRepository.findById(1L)).thenReturn(Optional.of(game));

        GameResponse response = gameService.findById(1L);

        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getTitle()).isEqualTo("Silksong");
    }

    @Test
    void findById_whenGameDoesNotExist_throwsResourceNotFoundException() {
        when(gameRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> gameService.findById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");
    }


    @Test
    void deleteById_whenGameExists_deletesGame() {
        Game game = new Game();
        game.setId(1L);
        when(gameRepository.findById(1L)).thenReturn(Optional.of(game));

        gameService.deleteById(1L);

        verify(gameRepository).deleteById(1L);
    }

    @Test
    void deleteById_whenGameDoesNotExist_throwsResourceNotFoundException() {
        when(gameRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> gameService.deleteById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");

        verify(gameRepository, never()).deleteById(any());
    }


    @Test
    void save_withValidRequest_returnsGameResponse() {
        GameRequest request = new GameRequest();
        request.setTitle("Silksong");
        request.setDescription("Metroidvania");
        request.setReleaseYear(2026);
        request.setGenreIds(List.of(10L));
        request.setPlatformIds(List.of(20L));

        Genre genre = new Genre();
        genre.setId(10L);

        Platform platform = new Platform();
        platform.setId(20L);

        when(genreRepository.findById(10L)).thenReturn(Optional.of(genre));
        when(platformRepository.findById(20L)).thenReturn(Optional.of(platform));

        Game savedGame = new Game();
        savedGame.setId(1L);
        savedGame.setTitle("Silksong");
        savedGame.setGenres(List.of(genre));
        savedGame.setPlatforms(List.of(platform));

        when(gameRepository.save(any(Game.class))).thenReturn(savedGame);

        GameResponse response = gameService.save(request);

        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getTitle()).isEqualTo("Silksong");
        assertThat(response.getGenres()).hasSize(1);
        assertThat(response.getPlatforms()).hasSize(1);
        verify(gameRepository).save(any(Game.class));
    }

    @Test
    void save_whenGenreDoesNotExist_throwsResourceNotFoundException() {
        GameRequest request = new GameRequest();
        request.setGenreIds(List.of(99L));

        when(genreRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> gameService.save(request))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Genre not found with id: 99");

        verify(gameRepository, never()).save(any());
    }

    @Test
    void save_whenPlatformDoesNotExist_throwsResourceNotFoundException() {
        GameRequest request = new GameRequest();
        request.setGenreIds(List.of(10L));
        request.setPlatformIds(List.of(88L));

        Genre genre = new Genre();
        genre.setId(10L);

        when(genreRepository.findById(10L)).thenReturn(Optional.of(genre));
        when(platformRepository.findById(88L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> gameService.save(request))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Platform not found with id: 88");

        verify(gameRepository, never()).save(any());
    }

    @Test
    void findAll_returnsListOfGameResponses() {
        Game game1 = new Game();
        game1.setId(1L);
        game1.setTitle("Silksong");

        Game game2 = new Game();
        game2.setId(2L);
        game2.setTitle("Hades 2");

        when(gameRepository.findAll(any(Specification.class))).thenReturn(List.of(game1, game2));

        List<GameResponse> responses = gameService.findAll("s", null, null);

        assertThat(responses).hasSize(2);
        assertThat(responses.get(0).getTitle()).isEqualTo("Silksong");
        assertThat(responses.get(1).getTitle()).isEqualTo("Hades 2");

        verify(gameRepository).findAll(any(Specification.class));
    }
}