package com.sch.gamelist_api.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@JsonPropertyOrder( { "id", "name"} )
public class GenreResponse {
    private Long id;
    private String name;
}
