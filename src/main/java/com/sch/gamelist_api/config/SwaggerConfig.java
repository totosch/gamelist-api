package com.sch.gamelist_api.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Gamelist API")
                        .description("REST API for managing a video game list")
                        .version("1.0.0")
                )
                .servers(List.of(
                        new io.swagger.v3.oas.models.servers.Server().url("https://gamelist-api-production-7e74.up.railway.app").description("Production"),
                        new io.swagger.v3.oas.models.servers.Server().url("http://localhost:8080").description("Local")
                ));
    }
}