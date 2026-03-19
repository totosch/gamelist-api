package com.sch.gamelist_api.service;


import com.sch.gamelist_api.dto.GenreRequest;
import com.sch.gamelist_api.dto.GenreResponse;
import com.sch.gamelist_api.exception.ResourceNotFoundException;
import com.sch.gamelist_api.model.Game;
import com.sch.gamelist_api.model.Genre;
import com.sch.gamelist_api.repository.GameRepository;
import com.sch.gamelist_api.repository.GenreRepository;
import com.sch.gamelist_api.repository.PlatformRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GenreServiceTest {

    @Mock
    private GenreRepository genreRepository;

    @InjectMocks
    private GenreService genreService;

    @Test
    void findById_whenGenreExists_returnsGenreResponse() {
        Genre genre = new Genre();
        genre.setId(1L);
        genre.setName("Metroidvania");

        when(genreRepository.findById(1L)).thenReturn(Optional.of(genre));

        GenreResponse response = genreService.findById(1L);

        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getName()).isEqualTo("Metroidvania");
    }

    @Test
    void findById_WhenGenreDoesNotExists_throwsResourseNotFoundException() {
        when(genreRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> genreService.findById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void deleteById_whenGenreExists_deletesGenre() {
        Genre genre = new Genre();
        genre.setId(1L);
        genre.setName("Metroidvania");

        when(genreRepository.findById(1L)).thenReturn(Optional.of(genre));

        genreService.deleteById(1L);
        verify(genreRepository).deleteById(1L);
    }

    @Test
    void deleteById_whenGenreDoesNotExist_throwsResourceNotFoundException() {
        when(genreRepository.findById(99L)).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> genreService.deleteById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");

        verify(genreRepository, never()).deleteById(any());
    }

    @Test
    void save_withValidRequest_returnGenreResponse() {
        GenreRequest request = new GenreRequest();
        request.setName("Metroidvania");

        Genre savedGenre = new Genre();
        savedGenre.setId(1L);
        savedGenre.setName("Metroidvania");

        when(genreRepository.save(any(Genre.class))).thenReturn(savedGenre);

        GenreResponse response = genreService.save(request);

        Assertions.assertThat(response.getId()).isEqualTo(1L);
        Assertions.assertThat(response.getName()).isEqualTo("Metroidvania");
        verify(genreRepository).save(any(Genre.class));
    }

}
