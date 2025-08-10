package com.example.EventManageProject.Services;




import com.example.EventManageProject.Dto.UserDto;
import com.example.EventManageProject.Entity.Role;
import com.example.EventManageProject.Entity.User;
import com.example.EventManageProject.Repositery.UserRepository;
import com.example.EventManageProject.Service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    private UserRepository userRepository;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        userService = new UserService(userRepository);
    }

    @Test
    @DisplayName("Should return total user count")
    void testCountUsers() {
        when(userRepository.count()).thenReturn(10L);

        long result = userService.countUsers();

        assertThat(result).isEqualTo(10);
        verify(userRepository).count();
    }

    @Test
    @DisplayName("Should return user by email")
    void testGetUserByEmail() {
        User user = new User();
        user.setEmail("john@example.com");

        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(user));

        User result = userService.getUserByEmail("john@example.com");

        assertThat(result.getEmail()).isEqualTo("john@example.com");
    }

    @Test
    @DisplayName("Should throw exception if user not found by email")
    void testGetUserByEmail_NotFound() {
        when(userRepository.findByEmail("notfound@example.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getUserByEmail("notfound@example.com"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("User not found with email");
    }

    @Test
    @DisplayName("Should return user by ID")
    void testGetUserById() {
        User user = new User();
        user.setId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User result = userService.getUserById(1L);

        assertThat(result.getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("Should throw exception if user not found by ID")
    void testGetUserById_NotFound() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getUserById(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("User not found with ID");
    }

    @Test
    @DisplayName("Should register user with encoded password and default role")
    void testRegisterUser() {
        UserDto dto = new UserDto();
        dto.setName("Alice");
        dto.setEmail("alice@example.com");
        dto.setPassword("password123");
        dto.setPhoneNumber("1234567890");
        dto.setAddress("123 Main St");

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);

        userService.registerUser(dto);

        verify(userRepository).save(userCaptor.capture());
        User savedUser = userCaptor.getValue();

        assertThat(savedUser.getName()).isEqualTo("Alice");
        assertThat(savedUser.getEmail()).isEqualTo("alice@example.com");
        assertThat(savedUser.getRole()).isEqualTo(Role.USER);
        assertThat(savedUser.isEnabled()).isTrue();
        assertThat(new BCryptPasswordEncoder().matches("password123", savedUser.getPassword())).isTrue();
    }
}
