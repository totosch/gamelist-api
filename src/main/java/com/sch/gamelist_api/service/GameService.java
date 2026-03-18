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
import com.sch.gamelist_api.specification.GameSpecification;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class GameService {

    private final GameRepository gameRepository;
    private final GenreRepository genreRepository;
    private final PlatformRepository platformRepository;

    public GameService(GameRepository gameRepository,
                       GenreRepository genreRepository,
                       PlatformRepository platformRepository) {
        this.gameRepository = gameRepository;
        this.genreRepository = genreRepository;
        this.platformRepository = platformRepository;
    }

    public GameResponse save(GameRequest request) {
        Game game = new Game();
        game.setTitle(request.getTitle());
        game.setDescription(request.getDescription());
        game.setReleaseYear(request.getReleaseYear());

        List<Genre> genres = new ArrayList<>();
        for (Long genreId : request.getGenreIds()) {
            Genre genre = genreRepository.findById(genreId).orElseThrow(
                    () -> new ResourceNotFoundException("Genre not found with id: " + genreId)
            );
            genres.add(genre);
        }
        game.setGenres(genres);

        List<Platform> platforms = new ArrayList<>();
        for (Long platformId : request.getPlatformIds()) {
            Platform platform = platformRepository.findById(platformId).orElseThrow(
                    () -> new ResourceNotFoundException("Platform not found with id: " + platformId)
            );
            platforms.add(platform);
        }
        game.setPlatforms(platforms);

        Game savedGame = gameRepository.save(game);
        return toResponse(savedGame);
    }

    private GameResponse toResponse(Game game) {
        GameResponse response = new GameResponse();
        response.setId(game.getId());
        response.setTitle(game.getTitle());
        response.setDescription(game.getDescription());
        response.setReleaseYear(game.getReleaseYear());
        response.setGenres(game.getGenres());
        response.setPlatforms(game.getPlatforms());
        return response;
    }

    public List<GameResponse> findAll(String title, Long genreId, Long platformId) {
        Specification<Game> spec = Specification
                .where(GameSpecification.titleContains(title))
                .and(GameSpecification.hasGenre(genreId))
                .and(GameSpecification.hasPlatform(platformId));

        List<Game> games = gameRepository.findAll(spec);
        List<GameResponse> responses = new ArrayList<>();
        for (Game game : games) {
            responses.add(toResponse(game));
        }
        return responses;
    }

    public GameResponse findById(Long id) {
        Game game = gameRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Game not found with id: " + id)
        );
        return toResponse(game);
    }

    public void deleteById(Long id) {
        Game game = gameRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Game not found with id: " + id));
        gameRepository.deleteById(id);
    }

}
