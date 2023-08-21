package ru.practicum.events.service;

import ru.practicum.events.dto.*;

import java.time.LocalDateTime;
import java.util.List;

public interface EventsService {
    FullOutputEventDto addEvent(Long userId, InputEventDto inputEventDto);

    List<ShortOutputEventDto> getUserEvents(Long userId, Integer from, Integer size);

    FullOutputEventDto getUserEventById(Long userId, Long eventId);

    FullOutputEventDto patchEvent(Long userId, Long eventId, UpdateEventDto updateEventDto);

    List<FullOutputEventDto> getFullInformationEvents(List<Long> users,
                                                      List<String> states,
                                                      List<Integer> categories,
                                                      LocalDateTime rangeStart,
                                                      LocalDateTime rangeEnd,
                                                      Integer from,
                                                      Integer size);

    FullOutputEventDto adminPatchEvent(Long eventId, UpdateEventDto updateEventDto);

    List<ShortOutputEventDto> searchEventsWithParam(String text,
                                                    List<Integer> categories,
                                                    Boolean paid,
                                                    LocalDateTime rangeStart,
                                                    LocalDateTime rangeEnd,
                                                    Boolean onlyAvailable,
                                                    String sort,
                                                    Integer from,
                                                    Integer size,
                                                    String ip);

    FullOutputEventDto getEventById(Long id, String ip);
}