package com.sch.gamelist_api.service;

import com.sch.gamelist_api.dto.GenreRequest;
import com.sch.gamelist_api.dto.GenreResponse;
import com.sch.gamelist_api.exception.ResourceNotFoundException;
import com.sch.gamelist_api.model.Genre;
import com.sch.gamelist_api.repository.GenreRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GenreService {

    private final GenreRepository genreRepository;

    public GenreService(GenreRepository genreRepository){
        this.genreRepository = genreRepository;
    }

    public List<GenreResponse> findAll() {
        List<Genre> genres = genreRepository.findAll();
        List<GenreResponse> responses = new ArrayList<>();
        for (Genre genre : genres) {
            responses.add(toResponse(genre));
        }
        return responses;
    }

    public GenreResponse findById(Long id) {
        Genre genre = genreRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Genre not found with id: " + id)
        );
        return toResponse(genre);
    }

    public GenreResponse save(GenreRequest request) {
        Genre genre = new Genre();
        genre.setName(request.getName());

        Genre savedGenre = genreRepository.save(genre);
        return toResponse(savedGenre);
    }

    public void deleteById(Long id) {
        genreRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Genre not found with id: " + id)
        );
        genreRepository.deleteById(id);
    }

    private GenreResponse toResponse(Genre genre) {
        GenreResponse response = new GenreResponse();
        response.setId(genre.getId());
        response.setName(genre.getName());
        return response;
    }

}
