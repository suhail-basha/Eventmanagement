package com.example.EventManageProject.Repositery;

import com.example.EventManageProject.Entity.Event;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class EventRepositoryTest {

    @Mock
    private EventRepository eventRepository;

    private Event event1;
    private Event event2;
    private Event event3;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        event1 = new Event();
        event1.setTitle("Tech Conference");
        event1.setVenue("New York");
        event1.setCategory("Technology");
        event1.setStartTime(LocalDateTime.now().plusDays(3));
        event1.setEndTime(LocalDateTime.now().plusDays(4));

        event2 = new Event();
        event2.setTitle("Art Exhibition");
        event2.setVenue("Paris");
        event2.setCategory("Art");
        event2.setStartTime(LocalDateTime.now().plusDays(1));
        event2.setEndTime(LocalDateTime.now().plusDays(2));

        event3 = new Event();
        event3.setTitle("Tech Meetup");
        event3.setVenue("New Jersey");
        event3.setCategory("Technology");
        event3.setStartTime(LocalDateTime.now().minusDays(1));
        event3.setEndTime(LocalDateTime.now());
    }

    @Test
    @DisplayName("Test findByFilters with location, category, and date")
    void testFindByFilters() {
        LocalDateTime date = event1.getStartTime().withHour(0).withMinute(0).withSecond(0).withNano(0);

        when(eventRepository.findByFilters("new", "Technology", date))
                .thenReturn(List.of(event1));

        List<Event> results = eventRepository.findByFilters("new", "Technology", date);

        assertThat(results).hasSize(1);
        assertThat(results.get(0).getTitle()).isEqualTo("Tech Conference");

        verify(eventRepository).findByFilters("new", "Technology", date);
    }

    @Test
    @DisplayName("Test findByFilters with null filters")
    void testFindByFiltersWithNulls() {
        when(eventRepository.findByFilters(null, null, null))
                .thenReturn(List.of(event1, event2, event3));

        List<Event> allEvents = eventRepository.findByFilters(null, null, null);
        assertThat(allEvents).hasSize(3);

        verify(eventRepository).findByFilters(null, null, null);
    }

    @Test
    @DisplayName("Test findDistinctCategories")
    void testFindDistinctCategories() {
        when(eventRepository.findDistinctCategories())
                .thenReturn(List.of("Technology", "Art"));

        List<String> categories = eventRepository.findDistinctCategories();
        assertThat(categories).containsExactlyInAnyOrder("Technology", "Art");

        verify(eventRepository).findDistinctCategories();
    }

    @Test
    @DisplayName("Test countByStartTimeAfter")
    void testCountByStartTimeAfter() {
        LocalDateTime now = LocalDateTime.now();
        when(eventRepository.countByStartTimeAfter(now)).thenReturn(2L);

        long count = eventRepository.countByStartTimeAfter(now);
        assertThat(count).isEqualTo(2);

        verify(eventRepository).countByStartTimeAfter(now);
    }
}
