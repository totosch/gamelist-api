package com.sch.gamelist_api.repository;

import com.sch.gamelist_api.model.Platform;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlatformRepository extends JpaRepository<Platform, Long> {
}
