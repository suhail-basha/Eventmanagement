package com.example.EventManageProject.Dto;

import lombok.Data;

@Data
public class SpeakerDto {
    private Long id;
    private String name;
    private String bio;
    private Long eventId;
}
