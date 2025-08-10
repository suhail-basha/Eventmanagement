package com.example.EventManageProject.Repositery;

import com.example.EventManageProject.Entity.Attendance;
import com.example.EventManageProject.Entity.Event;
import com.example.EventManageProject.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    boolean existsByUserAndEvent(User user, Event event);
    List<Attendance> findByEvent(Event event);
    List<Attendance> findByUser(User user);
    List<Attendance> findTop5ByOrderByAttendedAtDesc(); // For recent attendance records

}
