package com.sch.gamelist_api.dto;

import com.sch.gamelist_api.model.GameStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserGameResponse {

    private Long id;
    private String gameTitle;
    private GameStatus status;
    private Integer rating;
}