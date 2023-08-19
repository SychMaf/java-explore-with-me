package ru.practicum.events.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.category.dto.CategoryDtoMapper;
import ru.practicum.category.model.Category;
import ru.practicum.events.model.Event;
import ru.practicum.events.model.Location;
import ru.practicum.events.model.State;
import ru.practicum.user.dto.UserDtoMapper;
import ru.practicum.user.model.User;

import java.time.LocalDateTime;

@UtilityClass
public class EventMapper {

    public Event InputEventDtoToEvent(InputEventDto inputEventDto, User user, Category category) {
        return Event.builder()
                .annotation(inputEventDto.getAnnotation())
                .category(category)
                .createdOn(LocalDateTime.now())
                .description(inputEventDto.getDescription())
                .eventDate(inputEventDto.getEventDate())
                .initiator(user)
                .paid(inputEventDto.getPaid())
                .participantLimit(inputEventDto.getParticipantLimit())
                .requestModeration(inputEventDto.getRequestModeration())
                .title(inputEventDto.getTitle())
                .lat(inputEventDto.getLocation().getLat())
                .lon(inputEventDto.getLocation().getLon())
                .build();
    }

    public FullOutputEventDto eventToFullOutputEventDto(Event event) {
        return FullOutputEventDto.builder()
                .annotation(event.getAnnotation())
                .category(CategoryDtoMapper.toOutputCategoryDto(event.getCategory()))
                .confirmedRequests(event.getConfirmedRequest())
                .createdOn(event.getCreatedOn())
                .description(event.getDescription())
                .eventDate(event.getEventDate())
                .id(event.getId())
                .initiator(UserDtoMapper.toOutputUserDto(event.getInitiator()))
                .location(Location.builder()
                        .lon(event.getLon())
                        .lat(event.getLat())
                        .build())
                .paid(event.getPaid())
                .participantLimit(event.getParticipantLimit())
                .publishedOn(event.getPublishedOn())
                .requestModeration(event.getRequestModeration())
                .state(event.getState())
                .title(event.getTitle())
                .build();
    }

    public ShortOutputEventDto eventToShortOutputDto(Event event) {
        return ShortOutputEventDto.builder()
                .annotation(event.getAnnotation())
                .category(CategoryDtoMapper.toOutputCategoryDto(event.getCategory()))
                .confirmedRequests(event.getConfirmedRequest())
                .id(event.getId())
                .eventDate(event.getEventDate())
                .initiator(UserDtoMapper.toOutputUserDto(event.getInitiator()))
                .paid(event.getPaid())
                .title(event.getTitle())
                .build();
    }

    public Event updateEventUser(UpdateUserEventDto updateUserEventDto, Event event, Category category, State state) {
        return Event.builder()
                .annotation(updateUserEventDto.getAnnotation() != null ?
                        updateUserEventDto.getAnnotation() : event.getAnnotation())
                .category(category)
                .description(updateUserEventDto.getDescription() != null ?
                        updateUserEventDto.getDescription() : event.getDescription())
                .eventDate(updateUserEventDto.getEventDate() != null ?
                        updateUserEventDto.getEventDate() : event.getEventDate())
                .lon(updateUserEventDto.getLocation() != null ?
                        updateUserEventDto.getLocation().getLon() : event.getLon())
                .lat(updateUserEventDto.getLocation() != null ?
                        updateUserEventDto.getLocation().getLat() : event.getLat())
                .paid(updateUserEventDto.getPaid() != null ?
                        updateUserEventDto.getPaid() : event.getPaid())
                .participantLimit(updateUserEventDto.getParticipantLimit() != null ?
                        updateUserEventDto.getParticipantLimit() : event.getParticipantLimit())
                .requestModeration(updateUserEventDto.getRequestModeration() != null ?
                        updateUserEventDto.getRequestModeration() : event.getRequestModeration())
                .state(state)
                .title(updateUserEventDto.getTitle() != null ?
                        updateUserEventDto.getTitle() : event.getTitle())
                .confirmedRequest(event.getConfirmedRequest())
                .createdOn(event.getCreatedOn())
                .id(event.getId())
                .initiator(event.getInitiator())
                .publishedOn(event.getPublishedOn())
                .build();
    }
}
