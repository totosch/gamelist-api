package com.sch.gamelist_api.controller;

import com.sch.gamelist_api.dto.PlatformRequest;
import com.sch.gamelist_api.dto.PlatformResponse;
import com.sch.gamelist_api.service.PlatformService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/platforms")
public class PlatformController {

    private final PlatformService platformService;

    public PlatformController(PlatformService platformService) {
        this.platformService = platformService;
    }

    @GetMapping
    public ResponseEntity<List<PlatformResponse>> findAll() {
        List<PlatformResponse> platforms = platformService.findAll();
        return ResponseEntity.ok(platforms);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlatformResponse> findById(@PathVariable Long id) {
        PlatformResponse platform = platformService.findById(id);
        return ResponseEntity.ok(platform);
    }

    @SecurityRequirement(name = "bearerAuth")
    @PostMapping
    public ResponseEntity<PlatformResponse> save(@Valid @RequestBody PlatformRequest platform) {
        PlatformResponse savedPlatform = platformService.save(platform);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedPlatform);
    }

    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        platformService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
