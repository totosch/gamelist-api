package com.sch.gamelist_api.service;

import com.sch.gamelist_api.dto.AuthRequest;
import com.sch.gamelist_api.dto.AuthResponse;
import com.sch.gamelist_api.exception.ResourceNotFoundException;
import com.sch.gamelist_api.model.Role;
import com.sch.gamelist_api.model.User;
import com.sch.gamelist_api.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @InjectMocks AuthService authService;

    @Test
    void register_withNewEmail_returnsToken() {
        AuthRequest request = new AuthRequest();
        request.setEmail("test@test.com");
        request.setPassword("test");

        when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("test")).thenReturn("hashedPassword");
        when(userRepository.save(any(User.class))).thenReturn(new User());
        when(jwtService.generateToken("test@test.com")).thenReturn("fake-jwt");

        AuthResponse response = authService.register(request);

        assertThat(response.getToken()).isNotNull();
        assertThat(response.getToken()).isEqualTo("fake-jwt");
    }

    @Test
    void login_withCorrectCredentials_returnsToken() {
        AuthRequest request = new AuthRequest();
        request.setEmail("test@test.com");
        request.setPassword("123");

        User existingUser = new User();
        existingUser.setEmail("test@test.com");
        existingUser.setPassword("hashedPassword");
        existingUser.setRole(Role.USER);

        when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.of(existingUser));
        when(passwordEncoder.matches("123", "hashedPassword")).thenReturn(true);
        when(jwtService.generateToken("test@test.com")).thenReturn("fake-jwt");

        AuthResponse response = authService.login(request);

        assertThat(response.getToken()).isNotNull();
        assertThat(response.getToken()).isEqualTo("fake-jwt");

    }

    @Test
    void register_withExistingEmail_throwsIllegalArgumentException() {
        User existingUser = new User();
        existingUser.setEmail("test@test.com");
        existingUser.setPassword("hashedPassword");
        existingUser.setRole(Role.USER);

        AuthRequest request = new AuthRequest();
        request.setEmail("test@test.com");
        request.setPassword("123");

        when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.of(existingUser));

        assertThatThrownBy(() -> authService.register(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void login_withWrongPassword_throwsIllegalArgumentException() {
        AuthRequest request = new AuthRequest();
        request.setEmail("test@test.com");
        request.setPassword("123");

        User existingUser = new User();
        existingUser.setEmail("test@test.com");
        existingUser.setPassword("hashedPassword");
        existingUser.setRole(Role.USER);

        when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.of(existingUser));
        when(passwordEncoder.matches("123", "hashedPassword")).thenReturn(false);

        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void login_withNonExistentEmail_throwsResourceNotFoundException() {
        AuthRequest request = new AuthRequest();
        request.setEmail("test@test.com");
        request.setPassword("password123");

        when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("test@test.com");
    }

}
