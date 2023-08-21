package ru.practicum.events.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.category.model.Category;
import ru.practicum.category.repository.CategoryRepo;
import ru.practicum.events.dto.*;
import ru.practicum.events.model.AdminUpdateState;
import ru.practicum.events.model.Event;
import ru.practicum.events.model.State;
import ru.practicum.events.model.UserUpdateState;
import ru.practicum.events.repo.EventCriteria;
import ru.practicum.events.repo.EventRepo;
import ru.practicum.events.repo.EventSpecification;
import ru.practicum.events.repo.SortParam;
import ru.practicum.exception.exceptions.NotFoundException;
import ru.practicum.exception.exceptions.TimeException;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepo;

import javax.xml.bind.ValidationException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventsService {
    private final EventRepo eventRepo;
    private final UserRepo userRepo;
    private final CategoryRepo categoryRepo;

    @Override
    @Transactional
    public FullOutputEventDto addEvent(Long userId, InputEventDto inputEventDto) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id %d does not exist"));
        Category category = categoryRepo.findById(inputEventDto.getCategory())
                .orElseThrow(() -> new NotFoundException("User with id %d does not exist"));
        Event event = EventMapper.InputEventDtoToEvent(inputEventDto, user, category);
        event.setState(State.PENDING);
        event.setConfirmedRequest(0L);
        Event saveEvent = eventRepo.save(event);
        return EventMapper.eventToFullOutputEventDto(saveEvent);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ShortOutputEventDto> getUserEvents(Long userId, Integer from, Integer size) {
        if (!userRepo.existsById(userId)) {
            throw new NotFoundException("User with id %d does not exist");
        }
        Pageable pageable = PageRequest.of(from / size, size);
        return eventRepo.findAllByInitiator_Id(userId, pageable).stream()
                .map(EventMapper::eventToShortOutputDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public FullOutputEventDto getUserEventById(Long userId, Long eventId) {
        if (!userRepo.existsById(userId)) {
            throw new NotFoundException("User with id %d does not exist");
        }
        Event event = eventRepo.findAllByIdAndInitiator_Id(eventId, userId)
                .orElseThrow(() -> new NotFoundException("User dont have events with this id"));
        return EventMapper.eventToFullOutputEventDto(event);
    }

    @Override
    @Transactional
    public FullOutputEventDto patchEvent(Long userId, Long eventId, UpdateUserEventDto updateUserEventDto) {
        if (!userRepo.existsById(userId)) {
            throw new NotFoundException("User with id %d does not exist");
        }
        Event patchEvent = eventRepo.findAllByIdAndInitiator_Id(eventId, userId)
                .orElseThrow(() -> new NotFoundException("User dont have events with this id"));
        Category patchCategory = patchEvent.getCategory();
        if (updateUserEventDto.getCategory() != null) {
            patchCategory = categoryRepo.findById(updateUserEventDto.getCategory())
                    .orElseThrow(() -> new NotFoundException("Category with id %d does not exist"));
        }
        State patchState = patchEvent.getState();
        if (updateUserEventDto.getStateAction() != null) {
            UserUpdateState updateState = UserUpdateState.valueOf(updateUserEventDto.getStateAction());
            switch (updateState) {
                case CANCEL_REVIEW:
                    patchState = State.CANCELED;
                    break;
                case SEND_TO_REVIEW:
                    patchState = State.PENDING;
            }
        }
        patchEvent = eventRepo.save(EventMapper.updateEventUser(updateUserEventDto, patchEvent, patchCategory, patchState));
        return EventMapper.eventToFullOutputEventDto(patchEvent);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FullOutputEventDto> getFullInformationEvents(List<Long> users, List<String> states, List<Integer> categories, LocalDateTime rangeStart, LocalDateTime rangeEnd, Integer from, Integer size) {
        if (rangeStart != null && rangeEnd != null && rangeStart.isAfter(rangeEnd)) {
            throw new TimeException("Start time can not be after End time");
        }
        if (rangeStart == null && rangeEnd == null) {
            rangeStart = LocalDateTime.now();
        }
        Pageable pageable = PageRequest.of(from / size, size);
        List<State> convertStates = List.of();
        try {
            if (states != null) {
                convertStates = states.stream()
                        .map(State::valueOf)
                        .collect(Collectors.toList());
            }
        } catch (IllegalArgumentException e) {
            throw new RuntimeException();
        }
        EventCriteria criteria = EventCriteria.builder()
                .users(users)
                .states(!convertStates.isEmpty() ? convertStates : null)
                .categories(categories)
                .rangeStart(rangeStart)
                .rangeEnd(rangeEnd)
                .build();
        EventSpecification eventSpecification = new EventSpecification(criteria);
        return eventRepo.findAll(eventSpecification, pageable).stream()
                .map(EventMapper::eventToFullOutputEventDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public FullOutputEventDto adminPatchEvent(Long eventId, UpdateUserEventDto updateUserEventDto) {
        Event patchEvent = eventRepo.findById(eventId)
                .orElseThrow(() -> new NotFoundException("User dont have events with this id"));
        Category patchCategory = patchEvent.getCategory();
        if (updateUserEventDto.getCategory() != null) {
            patchCategory = categoryRepo.findById(updateUserEventDto.getCategory())
                    .orElseThrow(() -> new NotFoundException("Category with id %d does not exist"));
        }
        State patchState = patchEvent.getState();
        if (updateUserEventDto.getStateAction() != null) {
            AdminUpdateState updateState = AdminUpdateState.valueOf(updateUserEventDto.getStateAction());
            switch (updateState) {
                case PUBLISH_EVENT:
                    patchState = State.PUBLISHED;
                    patchEvent.setPublishedOn(LocalDateTime.now());
                    break;
                case CANCEL_EVENT:
                    patchState = State.CANCELED;
            }
        }
        patchEvent = eventRepo.save(EventMapper.updateEventUser(updateUserEventDto, patchEvent, patchCategory, patchState));
        return EventMapper.eventToFullOutputEventDto(patchEvent);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ShortOutputEventDto> searchEventsWithParam(String text, List<Integer> categories, Boolean paid, LocalDateTime rangeStart, LocalDateTime rangeEnd, Boolean onlyAvailable, String sort, Integer from, Integer size) {
        if (rangeStart != null && rangeEnd != null && rangeStart.isAfter(rangeEnd)) {
            throw new TimeException("Start time can not be after End time");
        }
        if (rangeStart == null && rangeEnd == null) {
            rangeStart = LocalDateTime.now();
        }
        Pageable pageable = PageRequest.of(from / size, size);
        EventCriteria criteria = EventCriteria.builder()
                .states(List.of(State.PUBLISHED))
                .text(text)
                .categories(categories)
                .rangeEnd(rangeEnd)
                .rangeStart(rangeStart)
                .onlyAvailable(onlyAvailable)
                .sortParam(sort != null ? SortParam.valueOf(sort) : null)
                .build();
        EventSpecification eventSpecification = new EventSpecification(criteria);
        return eventRepo.findAll(eventSpecification, pageable).stream()
                .map(EventMapper::eventToShortOutputDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public FullOutputEventDto getEventById(Long id) {
        return EventMapper.eventToFullOutputEventDto(eventRepo.findPublishedEventById(id)
                .orElseThrow(() -> new NotFoundException("User dont have events with this id")));
    }
}
