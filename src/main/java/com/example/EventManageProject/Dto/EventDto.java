package com.example.EventManageProject.Dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
@Data
public class EventDto {

    private Long id;
    private String title;
    private String description;
    private String category;
    private String venue;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    private byte[] image; // If you're sending image data (optional)

    private List<SpeakerDto> speakers; // Optional: speaker details if needed

    private boolean alreadyRegistered; // For checking if the user is already registered
    private boolean hasAttended;       // For attendance status

}
