package com.example.EventManageProject.Entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Event event;

    private boolean present;

    private LocalDateTime attendedAt;  // ✅ NEW FIELD

    // Constructors
    public Attendance() {}

    public Attendance(User user, Event event, boolean present) {
        this.user = user;
        this.event = event;
        this.present = present;
        this.attendedAt = LocalDateTime.now();  // default value
    }

    public Attendance(User user, Event event, boolean present, LocalDateTime attendedAt) {
        this.user = user;
        this.event = event;
        this.present = present;
        this.attendedAt = attendedAt;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public boolean isPresent() {
        return present;
    }

    public void setPresent(boolean present) {
        this.present = present;
    }

    public LocalDateTime getAttendedAt() {
        return attendedAt;
    }

    public void setAttendedAt(LocalDateTime attendedAt) {
        this.attendedAt = attendedAt;
    }
}
