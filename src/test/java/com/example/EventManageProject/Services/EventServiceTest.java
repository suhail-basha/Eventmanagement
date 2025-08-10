package com.example.EventManageProject.Services;



import com.example.EventManageProject.Dto.EventDto;
import com.example.EventManageProject.Entity.Event;
import com.example.EventManageProject.Repositery.EventRepository;
import com.example.EventManageProject.Service.EventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class EventServiceTest {

    private EventRepository eventRepository;
    private ModelMapper modelMapper;
    private EventService eventService;

    @BeforeEach
    void setUp() {
        eventRepository = mock(EventRepository.class);
        modelMapper = mock(ModelMapper.class);
        eventService = new EventService(eventRepository, modelMapper);
    }

    @Test
    @DisplayName("Should return all events mapped to DTOs")
    void testGetAllEvents() {
        Event event1 = new Event();
        event1.setId(1L);
        event1.setTitle("Java Conference");

        Event event2 = new Event();
        event2.setId(2L);
        event2.setTitle("Spring Boot Workshop");

        EventDto dto1 = new EventDto();
        dto1.setId(1L);
        dto1.setTitle("Java Conference");

        EventDto dto2 = new EventDto();
        dto2.setId(2L);
        dto2.setTitle("Spring Boot Workshop");

        when(eventRepository.findAll()).thenReturn(List.of(event1, event2));
        when(modelMapper.map(event1, EventDto.class)).thenReturn(dto1);
        when(modelMapper.map(event2, EventDto.class)).thenReturn(dto2);

        List<EventDto> result = (List<EventDto>) eventService.getAllEvents();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getTitle()).isEqualTo("Java Conference");
        assertThat(result.get(1).getTitle()).isEqualTo("Spring Boot Workshop");
    }

    @Test
    @DisplayName("Should return total event count")
    void testCountEvents() {
        when(eventRepository.count()).thenReturn(5L);

        long count = eventService.countEvents();

        assertThat(count).isEqualTo(5L);
    }

    @Test
    @DisplayName("Should return count of upcoming events")
    void testCountUpcomingEvents() {
        when(eventRepository.countByStartTimeAfter(any(LocalDateTime.class))).thenReturn(3L);

        long count = eventService.countUpcomingEvents();

        assertThat(count).isEqualTo(3L);
    }

    @Test
    @DisplayName("Should return event by ID")
    void testGetEventEntityById() {
        Event event = new Event();
        event.setId(1L);
        event.setTitle("Test Event");

        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));

        Event result = eventService.getEventEntityById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo("Test Event");
    }

    @Test
    @DisplayName("Should throw exception if event ID not found")
    void testGetEventEntityById_NotFound() {
        when(eventRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> eventService.getEventEntityById(1L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Event not found with id: 1");
    }

    @Test
    @DisplayName("Should search events with filters")
    void testSearchEvents() {
        Event event = new Event();
        event.setTitle("Filtered Event");

        when(eventRepository.findByFilters("New York", "Tech", LocalDateTime.of(2025, 1, 1, 0, 0)))
                .thenReturn(List.of(event));

        List<Event> results = eventService.searchEvents("New York", "Tech", LocalDateTime.of(2025, 1, 1, 0, 0));

        assertThat(results).hasSize(1);
        assertThat(results.get(0).getTitle()).isEqualTo("Filtered Event");
    }

    @Test
    @DisplayName("Should return distinct event categories")
    void testGetAllCategories() {
        when(eventRepository.findDistinctCategories()).thenReturn(List.of("Tech", "Art"));

        List<String> categories = eventService.getAllCategories();

        assertThat(categories).containsExactlyInAnyOrder("Tech", "Art");
    }
}

