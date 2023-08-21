package ru.practicum.validator;

import lombok.experimental.UtilityClass;
import ru.practicum.events.repo.EventRepo;
import ru.practicum.exception.exceptions.EventTimeException;
import ru.practicum.exception.exceptions.NotFoundException;

import java.time.LocalDateTime;

@UtilityClass
public class EventValidator {
    public void checkEventExist(EventRepo eventRepo, Long eventId) {
        if (!eventRepo.existsById(eventId)) {
            throw new NotFoundException("Event with id %d does not exist");
        }
    }

    public void checkEventStartTime(LocalDateTime rangeStart, LocalDateTime rangeEnd) {
        if (rangeStart != null && rangeEnd != null && rangeStart.isAfter(rangeEnd)) {
            throw new EventTimeException("Start time can not be after End time");
        }
    }
}
