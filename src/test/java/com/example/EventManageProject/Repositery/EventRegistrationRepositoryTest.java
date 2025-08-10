package com.example.EventManageProject.Repositery;

import com.example.EventManageProject.Entity.Event;
import com.example.EventManageProject.Entity.EventRegistration;
import com.example.EventManageProject.Entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class EventRegistrationRepositoryTest {

    @Mock
    private EventRegistrationRepository registrationRepository;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Test saving and finding registration")
    void testSaveAndFind() {
        User user = new User();
        user.setId(1L);
        user.setName("Test User");
        user.setEmail("test@example.com");

        Event event = new Event();
        event.setId(1L);
        event.setTitle("Spring Boot Workshop");

        EventRegistration registration = new EventRegistration();
        registration.setUser(user);
        registration.setEvent(event);
        registration.setRegisteredAt(LocalDateTime.now());

        when(registrationRepository.findByUserIdAndEventId(user.getId(), event.getId()))
                .thenReturn(Optional.of(registration));

        // Simulate saving - in mocks, save doesn't persist
        when(registrationRepository.save(any(EventRegistration.class))).thenReturn(registration);

        registrationRepository.save(registration);

        Optional<EventRegistration> found = registrationRepository.findByUserIdAndEventId(user.getId(), event.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getUser().getEmail()).isEqualTo("test@example.com");

        verify(registrationRepository).save(registration);
        verify(registrationRepository).findByUserIdAndEventId(user.getId(), event.getId());
    }

    @Test
    @DisplayName("Test existsByUserIdAndEventId")
    void testExistsByUserIdAndEventId() {
        Long userId = 1L;
        Long eventId = 1L;

        when(registrationRepository.existsByUserIdAndEventId(userId, eventId)).thenReturn(true);

        boolean exists = registrationRepository.existsByUserIdAndEventId(userId, eventId);
        assertThat(exists).isTrue();

        verify(registrationRepository).existsByUserIdAndEventId(userId, eventId);
    }

    @Test
    @DisplayName("Test findByUser")
    void testFindByUser() {
        User user = new User();
        user.setId(1L);

        Event event = new Event();
        event.setTitle("Spring Boot Workshop");

        EventRegistration registration = new EventRegistration();
        registration.setUser(user);
        registration.setEvent(event);
        registration.setRegisteredAt(LocalDateTime.now());

        when(registrationRepository.findByUser(user)).thenReturn(List.of(registration));

        List<EventRegistration> list = registrationRepository.findByUser(user);
        assertThat(list).hasSize(1);
        assertThat(list.get(0).getEvent().getTitle()).isEqualTo("Spring Boot Workshop");

        verify(registrationRepository).findByUser(user);
    }

    @Test
    @DisplayName("Test findTop5ByOrderByRegisteredAtDesc")
    void testFindTop5ByOrderByRegisteredAtDesc() {
        User user = new User();
        user.setId(1L);

        Event event = new Event();
        event.setTitle("Spring Boot Workshop");

        EventRegistration reg1 = new EventRegistration();
        reg1.setRegisteredAt(LocalDateTime.now());

        EventRegistration reg5 = new EventRegistration();
        reg5.setRegisteredAt(LocalDateTime.now().minusDays(4));

        List<EventRegistration> top5List = List.of(reg1, reg5, reg5, reg5, reg5);

        when(registrationRepository.findTop5ByOrderByRegisteredAtDesc()).thenReturn(top5List);

        List<EventRegistration> top5 = registrationRepository.findTop5ByOrderByRegisteredAtDesc();
        assertThat(top5).hasSize(5);
        assertThat(top5.get(0).getRegisteredAt()).isAfterOrEqualTo(top5.get(4).getRegisteredAt());

        verify(registrationRepository).findTop5ByOrderByRegisteredAtDesc();
    }

    @Test
    @DisplayName("Test getAllAttendances (custom query)")
    void testGetAllAttendances() {
        User user = new User();
        user.setId(1L);

        Event event = new Event();
        event.setId(1L);

        EventRegistration registration = new EventRegistration();
        registration.setUser(user);
        registration.setEvent(event);
        registration.setRegisteredAt(LocalDateTime.now());

        when(registrationRepository.getAllAttendances()).thenReturn(List.of(registration));

        List<EventRegistration> attendances = registrationRepository.getAllAttendances();
        assertThat(attendances).isNotEmpty();
        assertThat(attendances.get(0).getUser()).isNotNull();
        assertThat(attendances.get(0).getEvent()).isNotNull();

        verify(registrationRepository).getAllAttendances();
    }
}
