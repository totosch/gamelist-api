package com.sch.gamelist_api.service;

import com.sch.gamelist_api.exception.ResourceNotFoundException;
import com.sch.gamelist_api.model.Platform;
import com.sch.gamelist_api.repository.PlatformRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlatformService {

    private final PlatformRepository platformRepository;

    public PlatformService (PlatformRepository platformRepository) {
        this.platformRepository = platformRepository;
    }

    public List<Platform> findAll() {
        return platformRepository.findAll();
    }

    public Platform findById(Long id) {
        Platform platform = platformRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Platform not found with id: " + id)
        );
        return platform;
    }

    public Platform save(Platform platform) {
        return platformRepository.save(platform);
    }

    public void deleteById(Long id) {
        platformRepository.deleteById(id);
    }
}
