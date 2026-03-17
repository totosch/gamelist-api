package com.sch.gamelist_api.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.sch.gamelist_api.model.Genre;
import com.sch.gamelist_api.model.Platform;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonPropertyOrder({"id", "title", "description", "releaseYear", "genres", "platforms"})
public class GameResponse {
    private Long id;
    private String title;
    private String description;
    private Integer releaseYear;
    private List<Genre> genres;
    private List<Platform> platforms;
}