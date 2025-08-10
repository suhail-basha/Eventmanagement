package com.example.EventManageProject.Service;

import com.example.EventManageProject.Entity.Attendance;
import com.example.EventManageProject.Entity.Event;
import com.example.EventManageProject.Entity.User;

import com.example.EventManageProject.Repositery.AttendanceRepository;
import com.example.EventManageProject.Repositery.EventRegistrationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final EventRegistrationRepository registrationRepository;

    public AttendanceService(AttendanceRepository attendanceRepository, EventRegistrationRepository registrationRepository) {
        this.attendanceRepository = attendanceRepository;
        this.registrationRepository = registrationRepository;
    }

    public void markAttendance(User user, Event event, boolean present) {
        if (!attendanceRepository.existsByUserAndEvent(user, event)) {
            Attendance attendance = new Attendance(user, event, present);
            attendanceRepository.save(attendance);
        }
    }

    public boolean hasUserAttended(User user, Event event) {
        return attendanceRepository.existsByUserAndEvent(user, event);
    }

    public List<Attendance> getAttendanceForEvent(Event event) {
        return attendanceRepository.findByEvent(event);
    }

    public List<Attendance> getAttendanceForUser(User user) {
        return attendanceRepository.findByUser(user);
    }
    public long countAttendances() {
        return attendanceRepository.count();
    }

    public double calculateAttendanceRate() {
        long totalRegistrations = registrationRepository.count();
        long totalAttendances = attendanceRepository.count();

        if (totalRegistrations == 0) return 0.0;
        return (double) totalAttendances / totalRegistrations * 100;
    }

    public List<Attendance> getRecentAttendances() {
        return attendanceRepository.findTop5ByOrderByAttendedAtDesc();
    }

}
