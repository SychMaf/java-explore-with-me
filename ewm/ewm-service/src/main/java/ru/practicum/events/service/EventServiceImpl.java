package ru.practicum.events.service;

import lombok.RequiredArgsConstructor;
import org.dtoPoint.events.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.category.model.Category;
import ru.practicum.category.repository.CategoryRepo;
import ru.practicum.client.Client;
import ru.practicum.dto.InputDto;
import ru.practicum.dto.OutputDto;
import ru.practicum.events.dto.*;
import ru.practicum.events.model.AdminUpdateState;
import ru.practicum.events.model.Event;
import ru.practicum.events.model.UserUpdateState;
import ru.practicum.events.repo.EventCriteria;
import ru.practicum.events.repo.EventRepo;
import ru.practicum.events.repo.EventSpecification;
import ru.practicum.events.repo.SortParam;
import ru.practicum.exception.exceptions.EventPatchConflictException;
import ru.practicum.exception.exceptions.EventStateConflictException;
import ru.practicum.exception.exceptions.IllegalStateException;
import ru.practicum.exception.exceptions.NotFoundException;
import org.dtoPoint.rate.StateRate;
import ru.practicum.rate.repo.RateRepo;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepo;
import ru.practicum.validator.EventValidator;
import ru.practicum.validator.UserValidator;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventsService {
    private final String app = "ewm-service";
    private final EventRepo eventRepo;
    private final UserRepo userRepo;
    private final CategoryRepo categoryRepo;
    private final RateRepo rateRepo;
    private final Client statsClient;

    @Override
    @Transactional
    public FullOutputEventDto addEvent(Long userId, InputEventDto inputEventDto) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id %d does not exist"));
        Category category = categoryRepo.findById(inputEventDto.getCategory())
                .orElseThrow(() -> new NotFoundException("User with id %d does not exist"));
        Event event = EventMapper.inputEventDtoToEvent(inputEventDto, user, category);
        event.setState(State.PENDING);
        event.setConfirmedRequest(0L);
        return EventMapper.eventToFullOutputEventDto(eventRepo.save(event));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ShortOutputEventDto> getUserEvents(Long userId, Integer from, Integer size) {
        UserValidator.checkUserExist(userRepo, userId);
        Pageable pageable = PageRequest.of(from / size, size);
        List<Event> events = eventRepo.findAllByInitiator_Id(userId, pageable);
        events = fillEventsRating(events);
        return fillEventsHit(events).stream()
                .map(EventMapper::eventToShortOutputDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public FullOutputEventDto getUserEventById(Long userId, Long eventId) {
        UserValidator.checkUserExist(userRepo, userId);
        Event event = eventRepo.findAllByIdAndInitiator_Id(eventId, userId)
                .orElseThrow(() -> new NotFoundException("User dont have events with this id"));
        event = fillEventsRating(List.of(event)).get(0);
        return EventMapper.eventToFullOutputEventDto(fillEventsHit(List.of(event)).get(0));
    }

    @Override
    @Transactional
    public FullOutputEventDto patchEvent(Long userId, Long eventId, UpdateEventDto updateEventDto) {
        UserValidator.checkUserExist(userRepo, userId);
        Event patchEvent = eventRepo.findAllByIdAndInitiator_Id(eventId, userId)
                .orElseThrow(() -> new NotFoundException("User dont have events with this id"));
        if (!(patchEvent.getState().equals(State.PENDING) || patchEvent.getState().equals(State.REJECT_EVENT))) {
            throw new EventPatchConflictException("This Event not in normal?(PENDING/REJECT_EVENT) state");
        }
        Category patchCategory = patchEvent.getCategory();
        if (updateEventDto.getCategory() != null) {
            patchCategory = categoryRepo.findById(updateEventDto.getCategory())
                    .orElseThrow(() -> new NotFoundException("Category with id %d does not exist"));
        }
        State patchState = patchEvent.getState();
        if (updateEventDto.getStateAction() != null) {
            try {
                UserUpdateState updateState = UserUpdateState.valueOf(updateEventDto.getStateAction());
                switch (updateState) {
                    case CANCEL_REVIEW:
                        patchState = State.CANCELED;
                        break;
                    case SEND_TO_REVIEW:
                        patchState = State.PENDING;
                }
            } catch (IllegalArgumentException e) {
                throw new IllegalStateException("Not correct event State in query");
            }
        }
        return EventMapper.eventToFullOutputEventDto(eventRepo.save(
                EventMapper.updateEvent(updateEventDto, patchEvent, patchCategory, patchState)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<FullOutputEventDto> getFullInformationEvents(List<Long> users, List<String> states, List<Integer> categories, LocalDateTime rangeStart, LocalDateTime rangeEnd, Integer from, Integer size) {
        EventValidator.checkEventStartTime(rangeStart, rangeEnd);
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
            throw new IllegalStateException("Not correct event State in query");
        }
        EventCriteria criteria = EventCriteria.builder()
                .users(users)
                .states(!convertStates.isEmpty() ? convertStates : null)
                .categories(categories)
                .rangeStart(rangeStart)
                .rangeEnd(rangeEnd)
                .build();
        EventSpecification eventSpecification = new EventSpecification(criteria);
        List<Event> events = eventRepo.findAll(eventSpecification, pageable).toList();
        events = fillEventsRating(events);
        events = fillEventsHit(events);
        return fillEventsHit(events).stream()
                .map(EventMapper::eventToFullOutputEventDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public FullOutputEventDto adminPatchEvent(Long eventId, UpdateEventDto updateEventDto) {
        Event patchEvent = eventRepo.findById(eventId)
                .orElseThrow(() -> new NotFoundException("User dont have events with this id"));
        if (patchEvent.getState().equals(State.PUBLISHED) || patchEvent.getState().equals(State.REJECT_EVENT)) {
            throw new EventStateConflictException("Attempt patch PUBLISHED/REJECT_EVENT Event");
        }
        Category patchCategory = patchEvent.getCategory();
        if (updateEventDto.getCategory() != null) {
            patchCategory = categoryRepo.findById(updateEventDto.getCategory())
                    .orElseThrow(() -> new NotFoundException("Category with id %d does not exist"));
        }
        State patchState = patchEvent.getState();
        if (updateEventDto.getStateAction() != null) {
            AdminUpdateState updateState = AdminUpdateState.valueOf(updateEventDto.getStateAction());
            switch (updateState) {
                case PUBLISH_EVENT:
                    patchState = State.PUBLISHED;
                    patchEvent.setPublishedOn(LocalDateTime.now());
                    break;
                case REJECT_EVENT:
                    patchState = State.REJECT_EVENT;
            }
        }
        return EventMapper.eventToFullOutputEventDto(eventRepo.save(
                EventMapper.updateEvent(updateEventDto, patchEvent, patchCategory, patchState)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ShortOutputEventDto> searchEventsWithParam(String text, List<Integer> categories, Boolean paid, LocalDateTime rangeStart, LocalDateTime rangeEnd, Boolean onlyAvailable, String sort, Integer from, Integer size, String ip) {
        EventValidator.checkEventStartTime(rangeStart, rangeEnd);
        statsClient.addNewRequest(InputDto.builder()
                .app(app)
                .uri("/events")
                .ip(ip)
                .timestamp(LocalDateTime.now())
                .build());
        if (rangeStart == null && rangeEnd == null) {
            rangeStart = LocalDateTime.now();
        }
        Pageable pageable = PageRequest.of(from / size, size);
        EventCriteria criteria = EventCriteria.builder()
                .states(List.of(State.PUBLISHED))
                .paid(paid)
                .text(text)
                .categories(categories.isEmpty() ? null : categories)
                .rangeEnd(rangeEnd)
                .rangeStart(rangeStart)
                .onlyAvailable(onlyAvailable)
                .sortParam(sort != null ? SortParam.valueOf(sort) : null)
                .build();
        EventSpecification eventSpecification = new EventSpecification(criteria);
        List<Event> events = eventRepo.findAll(eventSpecification, pageable).toList();
        events = fillEventsRating(events);
        events = fillEventsHit(events);
        return events.stream()
                .map(EventMapper::eventToShortOutputDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public FullOutputEventDto getEventById(Long id, String ip) {
        Event event = eventRepo.findPublishedEventById(id)
                .orElseThrow(() -> new NotFoundException("User dont have events with this id"));
        statsClient.addNewRequest(InputDto.builder()
                .app(app)
                .uri("/events/" + id)
                .ip(ip)
                .timestamp(LocalDateTime.now())
                .build());
        event = fillEventsRating(List.of(event)).get(0);
        return EventMapper.eventToFullOutputEventDto(fillEventsHit(List.of(event)).get(0));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ShortOutputEventDto> getLikedUserEvents(Long userId) {
        UserValidator.checkUserExist(userRepo, userId);
        List<Event> events = rateRepo.findAllByRatePK_User_Id(userId).stream()
                .map(rate -> rate.getRatePK().getEvent())
                .collect(Collectors.toList());
        events = fillEventsRating(events);
        events = fillEventsHit(events);
        return events.stream()
                .map(EventMapper::eventToShortOutputDto)
                .collect(Collectors.toList());
    }

    private List<Event> fillEventsHit(List<Event> events) {
        List<OutputDto> getStat = statsClient.getStats(
                LocalDateTime.now().minusDays(1).truncatedTo(ChronoUnit.SECONDS),
                LocalDateTime.now().plusMinutes(1),
                events.stream()
                        .map(event -> "/events/" + event.getId())
                        .collect(Collectors.toList()),
                true);
        Map<Long, Long> result = new HashMap<>();
        getStat.forEach(st -> result.put(Long.valueOf(st.getUri().substring(st.getUri().lastIndexOf("/") + 1)), st.getHits()));
        return events.stream()
                .peek(event -> event.setViews(result.get(event.getId()) != null ? result.get(event.getId()) : 0))
                .collect(Collectors.toList());
    }

    private List<Event> fillEventsRating(List<Event> events) {
        return events.stream()
                .peek(event -> event.setCountLikes(rateRepo.findCountRates(event.getId(), StateRate.LIKE)))
                .peek(event -> event.setCountLikes(rateRepo.findCountRates(event.getId(), StateRate.LIKE)))
                .collect(Collectors.toList());
    }
}
