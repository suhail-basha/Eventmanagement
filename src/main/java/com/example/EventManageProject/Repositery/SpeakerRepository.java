package com.example.EventManageProject.Repositery;

import com.example.EventManageProject.Entity.Event;
import com.example.EventManageProject.Entity.Speaker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpeakerRepository extends JpaRepository<Speaker, Long> {
    List<Speaker> findByEventId(Long eventId);
    List<Speaker> findByEvent(Event event);


}
