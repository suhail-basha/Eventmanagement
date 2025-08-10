package com.example.EventManageProject.Service;

import com.example.EventManageProject.Entity.Event;
import com.example.EventManageProject.Entity.Speaker;
import com.example.EventManageProject.Repositery.EventRepository;
import com.example.EventManageProject.Repositery.SpeakerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import org.modelmapper.ModelMapper;
@Service
@RequiredArgsConstructor
public class SpeakerService {

    private final SpeakerRepository speakerRepository;
    private final EventRepository eventRepository;
    private final ModelMapper modelMapper;

    public List<Speaker> getSpeakersByEvent(Event event) {
    return speakerRepository.findByEvent(event);
    }

    public List<Speaker> getAllSpeakers() {
        return speakerRepository.findAll();
    }


}

