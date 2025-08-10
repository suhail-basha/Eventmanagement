package com.example.EventManageProject.Repositery;

import com.example.EventManageProject.Entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class UserRepositoryTest {

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindByEmail() {
        User user = new User();
        user.setName("Jane Doe");
        user.setEmail("jane.doe@example.com");
        user.setPassword("secure123");

        // Stub save to just return the user (simulate persistence)
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Stub findByEmail to return Optional.of(user) when queried with user's email
        when(userRepository.findByEmail("jane.doe@example.com")).thenReturn(Optional.of(user));

        // Call save (optional in mock, depends if you want to simulate it)
        User savedUser = userRepository.save(user);
        assertThat(savedUser).isEqualTo(user);

        // Call findByEmail and verify returned user
        Optional<User> found = userRepository.findByEmail("jane.doe@example.com");
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Jane Doe");

        // Verify interactions
        verify(userRepository).save(user);
        verify(userRepository).findByEmail("jane.doe@example.com");
    }

    @Test
    void testFindByEmailNotFound() {
        when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        Optional<User> result = userRepository.findByEmail("nonexistent@example.com");
        assertThat(result).isEmpty();

        verify(userRepository).findByEmail("nonexistent@example.com");
    }
}
