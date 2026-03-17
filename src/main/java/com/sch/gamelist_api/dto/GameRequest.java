package com.sch.gamelist_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class GameRequest {

    @NotBlank(message = "Title is required")
    private String title;

    private String description;

    @NotNull(message = "Release year is required")
    @Min(value = 1950, message = "Release year must be after 1950")
    @Max(value = 2030, message = "Release year must be before 2030")
    private Integer releaseYear;

    private List<Long> genreIds = new ArrayList<>();
    private List<Long> platformIds = new ArrayList<>();
}