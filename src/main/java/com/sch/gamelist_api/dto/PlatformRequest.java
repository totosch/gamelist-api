package com.sch.gamelist_api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class PlatformRequest {
    @NotBlank(message = "Name is required")
    private String name;
}
