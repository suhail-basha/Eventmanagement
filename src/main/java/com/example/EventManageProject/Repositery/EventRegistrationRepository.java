package com.example.EventManageProject.Repositery;

import com.example.EventManageProject.Entity.Event;
import com.example.EventManageProject.Entity.EventRegistration;
import com.example.EventManageProject.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface EventRegistrationRepository extends JpaRepository<EventRegistration, Long> {
    boolean existsByUserIdAndEventId(Long userId, Long eventId);
    Optional<EventRegistration> findByUserIdAndEventId(Long userId, Long eventId);

    List<EventRegistration> findByUser(User user);
    List<EventRegistration> findTop5ByOrderByRegisteredAtDesc();
    @Query("SELECT r FROM EventRegistration r JOIN FETCH r.user JOIN FETCH r.event")
    List<EventRegistration> getAllAttendances();




}
