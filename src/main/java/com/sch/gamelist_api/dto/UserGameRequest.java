package com.sch.gamelist_api.dto;

import com.sch.gamelist_api.model.GameStatus;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserGameRequest {

    @NotNull(message = "Game id is required")
    private Long gameId;

    @NotNull(message = "Status is required")
    private GameStatus status;

    @Min(value = 1, message = "Rating must be between 1 and 10")
    @Max(value = 10, message = "Rating must be between 1 and 10")
    private Integer rating;
}