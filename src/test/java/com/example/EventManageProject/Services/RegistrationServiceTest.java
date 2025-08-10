package com.example.EventManageProject.Services;



import com.example.EventManageProject.Entity.Event;
import com.example.EventManageProject.Entity.EventRegistration;
import com.example.EventManageProject.Entity.User;
import com.example.EventManageProject.Repositery.EventRegistrationRepository;
import com.example.EventManageProject.Repositery.EventRepository;
import com.example.EventManageProject.Repositery.UserRepository;
import com.example.EventManageProject.Service.EmailService;
import com.example.EventManageProject.Service.RegistrationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class RegistrationServiceTest {

    private EventRepository eventRepository;
    private EventRegistrationRepository registrationRepository;
    private EventRegistrationRepository eventRegistrationRepository;
    private UserRepository userRepository;
    private EmailService emailService;
    private ModelMapper modelMapper;
    private RegistrationService registrationService;

    private User user;
    private Event event;
    private EventRegistration registration;

    @BeforeEach
    void setUp() {
        eventRepository = mock(EventRepository.class);
        registrationRepository = mock(EventRegistrationRepository.class);
        eventRegistrationRepository = mock(EventRegistrationRepository.class);
        userRepository = mock(UserRepository.class);
        emailService = mock(EmailService.class);
        modelMapper = mock(ModelMapper.class);

        registrationService = new RegistrationService(
                eventRepository,
                registrationRepository,
                userRepository,
                emailService,
                modelMapper,
                eventRegistrationRepository
        );

        user = new User();
        user.setId(1L);
        user.setEmail("john@example.com");

        event = new Event();
        event.setId(1L);
        event.setTitle("Spring Boot Event");

        registration = new EventRegistration();
        registration.setId(1L);
        registration.setUser(user);
        registration.setEvent(event);
        registration.setRegisteredAt(LocalDateTime.now());
    }

    @Test
    @DisplayName("Should return recent registrations")
    void testGetRecentRegistrations() {
        when(registrationRepository.findTop5ByOrderByRegisteredAtDesc()).thenReturn(List.of(registration));

        List<EventRegistration> result = registrationService.getRecentRegistrations();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getUser().getEmail()).isEqualTo("john@example.com");
    }

    @Test
    @DisplayName("Should return true if user is registered")
    void testIsUserRegistered() {
        when(registrationRepository.existsByUserIdAndEventId(1L, 1L)).thenReturn(true);

        boolean result = registrationService.isUserRegistered(1L, 1L);

        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("Should save event registration")
    void testSaveRegistration() {
        registrationService.saveRegistration(registration);
        verify(registrationRepository, times(1)).save(registration);
    }

    @Test
    @DisplayName("Should return registrations by user")
    void testGetRegistrationsByUser() {
        when(registrationRepository.findByUser(user)).thenReturn(List.of(registration));

        List<EventRegistration> result = registrationService.getRegistrationsByUser(user);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getEvent().getTitle()).isEqualTo("Spring Boot Event");
    }

    @Test
    @DisplayName("Should return registration by user and event")
    void testFindByUserIdAndEventId() {
        when(eventRegistrationRepository.findByUserIdAndEventId(1L, 1L)).thenReturn(Optional.of(registration));

        EventRegistration result = registrationService.findByUserIdAndEventId(1L, 1L);

        assertThat(result).isNotNull();
        assertThat(result.getUser().getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("Should return all registrations")
    void testGetAllRegistrations() {
        when(registrationRepository.findAll()).thenReturn(List.of(registration));

        List<EventRegistration> result = registrationService.getAllRegistrations();

        assertThat(result).hasSize(1);
    }

    @Test
    @DisplayName("Should return registration by ID")
    void testGetRegistrationById() {
        when(registrationRepository.findById(1L)).thenReturn(Optional.of(registration));

        EventRegistration result = registrationService.getRegistrationById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getEvent().getTitle()).isEqualTo("Spring Boot Event");
    }

    @Test
    @DisplayName("Should return all attendances using custom query")
    void testGetAllAttendances() {
        when(eventRegistrationRepository.getAllAttendances()).thenReturn(List.of(registration));

        List<EventRegistration> result = registrationService.getAllAttendances();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getUser()).isEqualTo(user);
    }
}

