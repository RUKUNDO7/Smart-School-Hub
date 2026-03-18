package com.smartschoolhub.service;

import com.smartschoolhub.domain.SchoolEvent;
import com.smartschoolhub.repository.EventRepository;
import com.smartschoolhub.service.dto.EventRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class EventService {
    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public List<SchoolEvent> getAll() {
        return eventRepository.findAll();
    }

    public SchoolEvent getById(Long id) {
        return eventRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Event not found: " + id));
    }

    public SchoolEvent create(EventRequest request) {
        SchoolEvent event = new SchoolEvent();
        apply(event, request);
        return eventRepository.save(event);
    }

    public SchoolEvent update(Long id, EventRequest request) {
        SchoolEvent event = getById(id);
        apply(event, request);
        return eventRepository.save(event);
    }

    public void delete(Long id) {
        SchoolEvent event = getById(id);
        eventRepository.delete(event);
    }

    private void apply(SchoolEvent event, EventRequest request) {
        event.setTitle(request.getTitle());
        event.setDescription(request.getDescription());
        event.setStartDate(request.getStartDate());
        event.setEndDate(request.getEndDate());
        event.setType(request.getType());
    }
}
