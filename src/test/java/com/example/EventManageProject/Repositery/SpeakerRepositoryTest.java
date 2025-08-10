package com.example.EventManageProject.Repositery;

import com.example.EventManageProject.Entity.Event;
import com.example.EventManageProject.Entity.Speaker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class SpeakerRepositoryTest {

    @Mock
    private SpeakerRepository speakerRepository;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Test findByEventId returns correct speakers")
    void testFindByEventId() {
        Event event = new Event();
        event.setId(1L);

        Speaker speaker1 = new Speaker();
        speaker1.setName("Alice Johnson");
        speaker1.setTopic("AI & Ethics");
        speaker1.setEvent(event);

        Speaker speaker2 = new Speaker();
        speaker2.setName("Bob Smith");
        speaker2.setTopic("Machine Learning");
        speaker2.setEvent(event);

        List<Speaker> speakers = List.of(speaker1, speaker2);

        when(speakerRepository.findByEventId(event.getId())).thenReturn(speakers);

        List<Speaker> result = speakerRepository.findByEventId(event.getId());

        assertThat(result).hasSize(2);
        assertThat(result).extracting("name")
                .containsExactlyInAnyOrder("Alice Johnson", "Bob Smith");

        verify(speakerRepository, times(1)).findByEventId(event.getId());
    }

    @Test
    @DisplayName("Test findByEvent returns correct speakers")
    void testFindByEvent() {
        Event event = new Event();
        event.setId(2L);

        Speaker speaker = new Speaker();
        speaker.setName("Emily Clark");
        speaker.setTopic("Modern Art Trends");
        speaker.setEvent(event);

        List<Speaker> speakers = List.of(speaker);

        when(speakerRepository.findByEvent(event)).thenReturn(speakers);

        List<Speaker> result = speakerRepository.findByEvent(event);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Emily Clark");

        verify(speakerRepository, times(1)).findByEvent(event);
    }
}
