package com.example.EventManageProject.Repositery;

import com.example.EventManageProject.Entity.Attendance;
import com.example.EventManageProject.Entity.Event;
import com.example.EventManageProject.Entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class AttendanceRepositoryTest {

    @Mock
    private AttendanceRepository attendanceRepository;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testExistsByUserAndEvent() {
        User user = new User();
        user.setName("Alice");
        user.setEmail("alice@example.com");

        Event event = new Event();
        event.setTitle("Conference");
        event.setDescription("Tech conference");
        event.setCategory("Technology");
        event.setStartTime(LocalDateTime.now().plusDays(1));
        event.setEndTime(LocalDateTime.now().plusDays(1).plusHours(4));
        event.setVenue("Convention Center");

        when(attendanceRepository.existsByUserAndEvent(user, event)).thenReturn(true);

        boolean exists = attendanceRepository.existsByUserAndEvent(user, event);

        assertThat(exists).isTrue();

        verify(attendanceRepository, times(1)).existsByUserAndEvent(user, event);
    }

    @Test
    void testFindByEvent() {
        Event event = new Event();
        event.setTitle("Community Meetup");
        event.setDescription("Networking event");
        event.setCategory("Social");
        event.setStartTime(LocalDateTime.now().minusDays(1));
        event.setEndTime(LocalDateTime.now().minusDays(1).plusHours(2));
        event.setVenue("City Hall");

        Attendance attendance1 = new Attendance();
        attendance1.setAttendedAt(LocalDateTime.now().minusDays(1).plusMinutes(10));

        Attendance attendance2 = new Attendance();
        attendance2.setAttendedAt(LocalDateTime.now().minusDays(1).plusMinutes(30));

        when(attendanceRepository.findByEvent(event)).thenReturn(List.of(attendance1, attendance2));

        List<Attendance> attendances = attendanceRepository.findByEvent(event);

        assertThat(attendances).hasSize(2);

        verify(attendanceRepository, times(1)).findByEvent(event);
    }

    @Test
    void testFindByUser() {
        User user = new User();
        user.setName("Dave");
        user.setEmail("dave@example.com");

        Attendance a1 = new Attendance();
        a1.setAttendedAt(LocalDateTime.now().minusHours(5));
        Attendance a2 = new Attendance();
        a2.setAttendedAt(LocalDateTime.now());

        when(attendanceRepository.findByUser(user)).thenReturn(List.of(a1, a2));

        List<Attendance> attendances = attendanceRepository.findByUser(user);

        assertThat(attendances).hasSize(2);

        verify(attendanceRepository, times(1)).findByUser(user);
    }

    @Test
    void testFindTop5ByOrderByAttendedAtDesc() {
        Attendance a1 = new Attendance();
        Attendance a2 = new Attendance();
        Attendance a3 = new Attendance();
        Attendance a4 = new Attendance();
        Attendance a5 = new Attendance();

        a1.setAttendedAt(LocalDateTime.now().minusDays(1));
        a2.setAttendedAt(LocalDateTime.now().minusDays(2));
        a3.setAttendedAt(LocalDateTime.now().minusDays(3));
        a4.setAttendedAt(LocalDateTime.now().minusDays(4));
        a5.setAttendedAt(LocalDateTime.now().minusDays(5));

        when(attendanceRepository.findTop5ByOrderByAttendedAtDesc())
                .thenReturn(List.of(a1, a2, a3, a4, a5));

        List<Attendance> recent = attendanceRepository.findTop5ByOrderByAttendedAtDesc();

        assertThat(recent).hasSize(5);

        verify(attendanceRepository, times(1)).findTop5ByOrderByAttendedAtDesc();
    }
}
