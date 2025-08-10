package com.example.EventManageProject.Dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EventRegistrationDto {
    private Long id;
    private Long eventId;
    private String eventTitle;
    private LocalDateTime registrationDate;
    private boolean attended;
    private LocalDateTime registeredAt;
}
