package com.sch.gamelist_api.service;


import com.sch.gamelist_api.dto.GenreRequest;
import com.sch.gamelist_api.dto.GenreResponse;
import com.sch.gamelist_api.dto.PlatformRequest;
import com.sch.gamelist_api.dto.PlatformResponse;
import com.sch.gamelist_api.exception.ResourceNotFoundException;
import com.sch.gamelist_api.model.Genre;
import com.sch.gamelist_api.model.Platform;
import com.sch.gamelist_api.repository.PlatformRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.management.PlatformLoggingMXBean;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PlatformServiceTest {

    @Mock
    private PlatformRepository platformRepository;

    @InjectMocks
    private PlatformService platformService;

    @Test
    void findById_whenPlatformExists_returnsPlatformResponse() {
        Platform platform = new Platform();
        platform.setId(1L);
        platform.setName("SNES");

        when(platformRepository.findById(1L)).thenReturn(Optional.of(platform));

        PlatformResponse response = platformService.findById(1L);

        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getName()).isEqualTo("SNES");
    }

    @Test
    void findById_WhenPlatformDoesNotExists_throwsResourseNotFoundException() {
        when(platformRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> platformService.findById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void deleteById_whenPlatformExists_deletesPlatform() {
        Platform platform = new Platform();
        platform.setId(1L);
        platform.setName("SNES");

        when(platformRepository.findById(1L)).thenReturn(Optional.of(platform));

        platformService.deleteById(1L);
        verify(platformRepository).deleteById(1L);
    }

    @Test
    void deleteById_whenPlatformDoesNotExist_throwsResourceNotFoundException() {
        when(platformRepository.findById(99L)).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> platformService.deleteById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");

        verify(platformRepository, never()).deleteById(any());
    }

    @Test
    void save_withValidRequest_returnPlatformResponse() {
        PlatformRequest request = new PlatformRequest();
        request.setName("SNES");

        Platform savedPlatform = new Platform();
        savedPlatform.setId(1L);
        savedPlatform.setName("SNES");

        when(platformRepository.save(any(Platform.class))).thenReturn(savedPlatform);

        PlatformResponse response = platformService.save(request);

        Assertions.assertThat(response.getId()).isEqualTo(1L);
        Assertions.assertThat(response.getName()).isEqualTo("SNES");
        verify(platformRepository).save(any(Platform.class));
    }


}
