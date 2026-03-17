package com.sch.gamelist_api.service;

import com.sch.gamelist_api.exception.ResourceNotFoundException;
import com.sch.gamelist_api.model.Genre;
import com.sch.gamelist_api.model.Platform;
import com.sch.gamelist_api.repository.GenreRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GenreService {

    private final GenreRepository genreRepository;

    public GenreService(GenreRepository genreRepository){
        this.genreRepository = genreRepository;
    }

    public List<Genre> findAll() {
        return genreRepository.findAll();
    }

    public Genre findById(Long id) {
        Genre genre = genreRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Genre not found with id: " + id)
        );
        return genre;
    }

    public Genre save(Genre genre) {
        return genreRepository.save(genre);
    }

    public void deleteById(Long id) {
        genreRepository.deleteById(id);
    }
}
