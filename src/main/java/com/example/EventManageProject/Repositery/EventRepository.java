package com.example.EventManageProject.Repositery;

import com.example.EventManageProject.Entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    @Query("SELECT e FROM Event e " +
            "WHERE (:location IS NULL OR LOWER(e.venue) LIKE LOWER(CONCAT('%', :location, '%'))) " +
            "AND (:category IS NULL OR e.category = :category) " +
            "AND (:date IS NULL OR DATE(e.startTime) = DATE(:date))")
    List<Event> findByFilters(@Param("location") String location,
                              @Param("category") String category,
                              @Param("date") LocalDateTime date);

    @Query("SELECT DISTINCT e.category FROM Event e")
    List<String> findDistinctCategories();
    long countByStartTimeAfter(LocalDateTime now);


}
