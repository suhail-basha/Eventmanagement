package com.example.EventManageProject.Services;



import com.example.EventManageProject.Entity.Event;
import com.example.EventManageProject.Entity.Speaker;
import com.example.EventManageProject.Repositery.EventRepository;
import com.example.EventManageProject.Repositery.SpeakerRepository;
import com.example.EventManageProject.Service.SpeakerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class SpeakerServiceTest {

    private SpeakerRepository speakerRepository;
    private EventRepository eventRepository;
    private ModelMapper modelMapper;
    private SpeakerService speakerService;

    private Event event;
    private Speaker speaker;

    @BeforeEach
    void setUp() {
        speakerRepository = mock(SpeakerRepository.class);
        eventRepository = mock(EventRepository.class);
        modelMapper = mock(ModelMapper.class);

        speakerService = new SpeakerService(speakerRepository, eventRepository, modelMapper);

        event = new Event();
        event.setId(1L);
        event.setTitle("Tech Talk");

        speaker = new Speaker();
        speaker.setId(1L);
        speaker.setName("Alice Johnson");
        speaker.setEvent(event);
    }

    @Test
    @DisplayName("Should return speakers by event")
    void testGetSpeakersByEvent() {
        when(speakerRepository.findByEvent(event)).thenReturn(List.of(speaker));

        List<Speaker> result = speakerService.getSpeakersByEvent(event);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Alice Johnson");
        verify(speakerRepository, times(1)).findByEvent(event);
    }

    @Test
    @DisplayName("Should return all speakers")
    void testGetAllSpeakers() {
        when(speakerRepository.findAll()).thenReturn(List.of(speaker));

        List<Speaker> result = speakerService.getAllSpeakers();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getEvent().getTitle()).isEqualTo("Tech Talk");
        verify(speakerRepository, times(1)).findAll();
    }
}

