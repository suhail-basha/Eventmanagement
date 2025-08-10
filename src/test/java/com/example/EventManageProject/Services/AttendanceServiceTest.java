package com.example.EventManageProject.Services;



import com.example.EventManageProject.Entity.Attendance;
import com.example.EventManageProject.Entity.Event;
import com.example.EventManageProject.Entity.User;
import com.example.EventManageProject.Repositery.AttendanceRepository;
import com.example.EventManageProject.Repositery.EventRegistrationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import com.example.EventManageProject.Service.AttendanceService;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class AttendanceServiceTest {

    private AttendanceRepository attendanceRepository;
    private EventRegistrationRepository registrationRepository;
    private AttendanceService attendanceService;

    private User user;
    private Event event;

    @BeforeEach
    void setUp() {
        attendanceRepository = mock(AttendanceRepository.class);
        registrationRepository = mock(EventRegistrationRepository.class);
        attendanceService = new AttendanceService(attendanceRepository, registrationRepository);

        user = new User();
        user.setId(1L);
        user.setName("John");
        user.setEmail("john@example.com");

        event = new Event();
        event.setId(1L);
        event.setTitle("Tech Talk");
    }

    @Test
    @DisplayName("Should mark attendance if not already present")
    void testMarkAttendance_New() {
        when(attendanceRepository.existsByUserAndEvent(user, event)).thenReturn(false);

        attendanceService.markAttendance(user, event, true);

        verify(attendanceRepository, times(1)).save(any(Attendance.class));
    }

    @Test
    @DisplayName("Should not mark attendance if already exists")
    void testMarkAttendance_Existing() {
        when(attendanceRepository.existsByUserAndEvent(user, event)).thenReturn(true);

        attendanceService.markAttendance(user, event, true);

        verify(attendanceRepository, never()).save(any(Attendance.class));
    }

    @Test
    @DisplayName("Should return true if user has attended")
    void testHasUserAttended() {
        when(attendanceRepository.existsByUserAndEvent(user, event)).thenReturn(true);

        boolean attended = attendanceService.hasUserAttended(user, event);

        assertThat(attended).isTrue();
    }

    @Test
    @DisplayName("Should return attendance list for an event")
    void testGetAttendanceForEvent() {
        Attendance attendance = new Attendance(user, event, true);
        when(attendanceRepository.findByEvent(event)).thenReturn(List.of(attendance));

        List<Attendance> list = attendanceService.getAttendanceForEvent(event);

        assertThat(list).hasSize(1);
        assertThat(list.get(0).getUser()).isEqualTo(user);
    }

    @Test
    @DisplayName("Should return attendance list for a user")
    void testGetAttendanceForUser() {
        Attendance attendance = new Attendance(user, event, true);
        when(attendanceRepository.findByUser(user)).thenReturn(List.of(attendance));

        List<Attendance> list = attendanceService.getAttendanceForUser(user);

        assertThat(list).hasSize(1);
        assertThat(list.get(0).getEvent()).isEqualTo(event);
    }

    @Test
    @DisplayName("Should return correct attendance count")
    void testCountAttendances() {
        when(attendanceRepository.count()).thenReturn(5L);

        long count = attendanceService.countAttendances();

        assertThat(count).isEqualTo(5L);
    }

    @Test
    @DisplayName("Should calculate attendance rate correctly")
    void testCalculateAttendanceRate() {
        when(attendanceRepository.count()).thenReturn(30L);
        when(registrationRepository.count()).thenReturn(60L);

        double rate = attendanceService.calculateAttendanceRate();

        assertThat(rate).isEqualTo(50.0);
    }

    @Test
    @DisplayName("Should return 0 attendance rate if no registrations")
    void testCalculateAttendanceRate_NoRegistrations() {
        when(registrationRepository.count()).thenReturn(0L);

        double rate = attendanceService.calculateAttendanceRate();

        assertThat(rate).isEqualTo(0.0);
    }

    @Test
    @DisplayName("Should return recent attendances")
    void testGetRecentAttendances() {
        Attendance a1 = new Attendance(user, event, true);
        a1.setAttendedAt(LocalDateTime.now());

        when(attendanceRepository.findTop5ByOrderByAttendedAtDesc()).thenReturn(List.of(a1));

        List<Attendance> recent = attendanceService.getRecentAttendances();

        assertThat(recent).hasSize(1);
        assertThat(recent.get(0).getUser().getEmail()).isEqualTo("john@example.com");
    }
}

