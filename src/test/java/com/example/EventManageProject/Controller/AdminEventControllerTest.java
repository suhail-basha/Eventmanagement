package com.example.EventManageProject.Controller;

import com.example.EventManageProject.Repositery.EventRegistrationRepository;
import com.example.EventManageProject.Repositery.EventRepository;
import com.example.EventManageProject.Repositery.SpeakerRepository;
import com.example.EventManageProject.Repositery.UserRepository;
import com.example.EventManageProject.Service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.mockito.Mockito;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Collections;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AdminEventControllerTest {

    private MockMvc mockMvc;

    private EventService eventService;
    private SpeakerService speakerService;
    private UserService userService;
    private RegistrationService registrationService;
    private AttendanceService attendanceService;

    private EventRepository eventRepository;
    private UserRepository userRepository;
    private EventRegistrationRepository eventRegistrationRepository;
    private SpeakerRepository speakerRepository;

    private AdminEventController adminEventController;

    @BeforeEach
    void setUp() {
        // Create mocks manually
        eventService = Mockito.mock(EventService.class);
        speakerService = Mockito.mock(SpeakerService.class);
        userService = Mockito.mock(UserService.class);
        registrationService = Mockito.mock(RegistrationService.class);
        attendanceService = Mockito.mock(AttendanceService.class);

        eventRepository = Mockito.mock(EventRepository.class);
        userRepository = Mockito.mock(UserRepository.class);
        eventRegistrationRepository = Mockito.mock(EventRegistrationRepository.class);
        speakerRepository = Mockito.mock(SpeakerRepository.class);

        // Create controller with mocks
        adminEventController = new AdminEventController(
                eventService,
                speakerService,
                userService,
                registrationService,
                eventRepository,
                userRepository,
                eventRegistrationRepository,
                attendanceService,
                speakerRepository
        );

        // Setup standalone MockMvc
        mockMvc = MockMvcBuilders.standaloneSetup(adminEventController).build();
    }

    @Test
    @DisplayName("GET /admin/dashboard - returns dashboard view with model attributes")
    void testDashboard() throws Exception {
        // Arrange mocks
        when(eventService.countEvents()).thenReturn(5L);
        when(userService.countUsers()).thenReturn(10L);
        when(eventService.countUpcomingEvents()).thenReturn(3L);
        when(attendanceService.countAttendances()).thenReturn(15L);
        when(attendanceService.calculateAttendanceRate()).thenReturn(75.0);
        when(registrationService.getRecentRegistrations()).thenReturn(Collections.emptyList());
        when(attendanceService.getRecentAttendances()).thenReturn(Collections.emptyList());

        // Act + Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/admin/dashboard"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/dashboard"))
                .andExpect(model().attributeExists(
                        "totalEvents", "totalUsers", "upcomingEvents",
                        "totalAttendances", "attendanceRate", "recentRegistrations", "recentAttendances"
                ));
    }

    @Test
    void testShowAllEvents() throws Exception {
        when(eventService.getAllEvents()).thenReturn(Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders.get("/admin/events"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/events"))
                .andExpect(model().attributeExists("events"));
    }

    @Test
    void testShowAddEventForm() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/admin/add-event"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/add-event"))
                .andExpect(model().attributeExists("event"));
    }

    @Test
    void testViewAllUsers() throws Exception {
        when(userRepository.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders.get("/admin/users"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/users"))
                .andExpect(model().attributeExists("users"));
    }

    @Test
    void testViewSpeakers() throws Exception {
        when(speakerService.getAllSpeakers()).thenReturn(Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders.get("/admin/speakers"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/manage-speakers"))
                .andExpect(model().attributeExists("speakers"));
    }

    @Test
    void testViewRegistrations() throws Exception {
        when(registrationService.getAllRegistrations()).thenReturn(Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders.get("/admin/registrations"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/registrations"))
                .andExpect(model().attributeExists("registrations"));
    }

    @Test
    void testViewAllAttendances() throws Exception {
        when(registrationService.getAllAttendances()).thenReturn(Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders.get("/admin/attendances"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/attendances"))
                .andExpect(model().attributeExists("attendances"));
    }
}
