package com.example.EventManageProject.Service;

import com.example.EventManageProject.Dto.EventDto;
import com.example.EventManageProject.Entity.Event;
import com.example.EventManageProject.Repositery.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final ModelMapper modelMapper;

    public Object getAllEvents() {
        return eventRepository.findAll().stream()
                .map(event -> modelMapper.map(event, EventDto.class))
                .collect(Collectors.toList());
    }
    public long countEvents() {
        return eventRepository.count();
    }

    public long countUpcomingEvents() {
        return eventRepository.countByStartTimeAfter(LocalDateTime.now());
    }

    public Event getEventEntityById(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found with id: " + id));
    }
    public List<Event> searchEvents(String location, String category, LocalDateTime date) {
        return eventRepository.findByFilters(location, category, date);
    }

    public List<String> getAllCategories() {
        return eventRepository.findDistinctCategories();
    }

}
