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

    public Event inputEventDtoToEvent(InputEventDto inputEventDto, User user, Category category) {
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
                .views(event.getViews())
                .countDislikes(event.getCountDislikes() != null ? event.getCountDislikes() : 0)
                .countLikes(event.getCountLikes() != null ? event.getCountLikes() : 0)
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
                .countDislikes(event.getCountDislikes() != null ? event.getCountDislikes() : 0)
                .countLikes(event.getCountLikes() != null ? event.getCountLikes() : 0)
                .build();
    }

    public Event updateEvent(UpdateEventDto updateEventDto, Event event, Category category, State state) {
        return Event.builder()
                .annotation(updateEventDto.getAnnotation() != null ?
                        updateEventDto.getAnnotation() : event.getAnnotation())
                .category(category)
                .description(updateEventDto.getDescription() != null ?
                        updateEventDto.getDescription() : event.getDescription())
                .eventDate(updateEventDto.getEventDate() != null ?
                        updateEventDto.getEventDate() : event.getEventDate())
                .lon(updateEventDto.getLocation() != null ?
                        updateEventDto.getLocation().getLon() : event.getLon())
                .lat(updateEventDto.getLocation() != null ?
                        updateEventDto.getLocation().getLat() : event.getLat())
                .paid(updateEventDto.getPaid() != null ?
                        updateEventDto.getPaid() : event.getPaid())
                .participantLimit(updateEventDto.getParticipantLimit() != null ?
                        updateEventDto.getParticipantLimit() : event.getParticipantLimit())
                .requestModeration(updateEventDto.getRequestModeration() != null ?
                        updateEventDto.getRequestModeration() : event.getRequestModeration())
                .state(state)
                .title(updateEventDto.getTitle() != null ?
                        updateEventDto.getTitle() : event.getTitle())
                .confirmedRequest(event.getConfirmedRequest())
                .createdOn(event.getCreatedOn())
                .id(event.getId())
                .initiator(event.getInitiator())
                .publishedOn(event.getPublishedOn())
                .views(event.getViews())
                .build();
    }
}
