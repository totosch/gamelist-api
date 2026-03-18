package com.sch.gamelist_api.controller;

import com.sch.gamelist_api.dto.UserGameRequest;
import com.sch.gamelist_api.dto.UserGameResponse;
import com.sch.gamelist_api.service.UserGameService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user/games")
public class UserGameController {

    private final UserGameService userGameService;

    public UserGameController(UserGameService userGameService) {
        this.userGameService = userGameService;
    }

    @GetMapping
    public ResponseEntity<List<UserGameResponse>> findAll(Authentication authentication) {
        String email = authentication.getName();
        List<UserGameResponse> games = userGameService.findByUserEmail(email);
        return ResponseEntity.ok(games);
    }

    @PostMapping
    public ResponseEntity<UserGameResponse> addGame(Authentication authentication,
                                                    @Valid @RequestBody UserGameRequest request) {
        String email = authentication.getName();
        UserGameResponse response = userGameService.addGame(email, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{gameId}")
    public ResponseEntity<UserGameResponse> updateGame(Authentication authentication,
                                                       @PathVariable Long gameId,
                                                       @Valid @RequestBody UserGameRequest request) {
        String email = authentication.getName();
        UserGameResponse response = userGameService.updateGame(email, gameId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{gameId}")
    public ResponseEntity<Void> removeGame(Authentication authentication,
                                           @PathVariable Long gameId) {
        String email = authentication.getName();
        userGameService.removeGame(email, gameId);
        return ResponseEntity.noContent().build();
    }
}