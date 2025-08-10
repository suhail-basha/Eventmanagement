package com.example.EventManageProject.Entity;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class EmailNotificationLog {
    @Id @GeneratedValue
    private Long id;

    private String recipient;
    private String subject;
    private LocalDateTime sentAt;
    private String type; //

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public LocalDateTime getSentAt() {
        return sentAt;
    }

    public void setSentAt(LocalDateTime sentAt) {
        this.sentAt = sentAt;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    // "REGISTRATION_CONFIRMATION", "REMINDER"
}
