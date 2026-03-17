package com.sch.gamelist_api.controller;

import com.sch.gamelist_api.model.Platform;
import com.sch.gamelist_api.service.PlatformService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/platform")
public class PlatformController {

    private final PlatformService platformService;

    public PlatformController(PlatformService platformService) {
        this.platformService = platformService;
    }

    @GetMapping
    public ResponseEntity<List<Platform>> findAll() {
        List<Platform> platforms = platformService.findAll();
        return ResponseEntity.ok(platforms);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Platform> findById(@PathVariable Long id) {
        Platform platform = platformService.findById(id);
        return ResponseEntity.ok(platform);
    }

    @PostMapping
    public ResponseEntity<Platform> save(@RequestBody Platform platform) {
        Platform savedPlatform = platformService.save(platform);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedPlatform);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        platformService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
