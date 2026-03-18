package com.sch.gamelist_api.service;

import com.sch.gamelist_api.dto.PlatformRequest;
import com.sch.gamelist_api.dto.PlatformResponse;
import com.sch.gamelist_api.exception.ResourceNotFoundException;
import com.sch.gamelist_api.model.Platform;
import com.sch.gamelist_api.repository.PlatformRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PlatformService {

    private final PlatformRepository platformRepository;

    public PlatformService (PlatformRepository platformRepository) {
        this.platformRepository = platformRepository;
    }

    public List<PlatformResponse> findAll() {
        List<Platform> platforms = platformRepository.findAll();
        List<PlatformResponse> responses = new ArrayList<>();
        for (Platform platform : platforms) {
            responses.add(toResponse(platform));
        }
        return responses;
    }

    public PlatformResponse findById(Long id) {
        Platform platform = platformRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Platform not found with id: " + id)
        );
        return toResponse(platform);
    }

    public PlatformResponse save(PlatformRequest request) {
        Platform platform = new Platform();
        platform.setName(request.getName());

        Platform savedPlatform = platformRepository.save(platform);
        return toResponse(savedPlatform);
    }

    public void deleteById(Long id) {
        platformRepository.deleteById(id);
    }

    private PlatformResponse toResponse(Platform platform) {
        PlatformResponse response = new PlatformResponse();
        response.setId(platform.getId());
        response.setName(platform.getName());
        return response;
    }
}
