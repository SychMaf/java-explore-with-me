package ru.practicum.requests.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.events.model.Event;
import ru.practicum.events.model.State;
import ru.practicum.events.repo.EventRepo;
import ru.practicum.exception.exceptions.NotFoundException;
import ru.practicum.exception.exceptions.RequestCreatedConflictException;
import ru.practicum.requests.dto.InputUpdateStatusRequestDto;
import ru.practicum.requests.dto.OutputRequestDto;
import ru.practicum.requests.dto.OutputUpdateStatusRequestsDto;
import ru.practicum.requests.dto.RequestMapper;
import ru.practicum.requests.model.Request;
import ru.practicum.requests.model.RequestStatus;
import ru.practicum.requests.repo.RequestRepo;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepo;
import ru.practicum.validator.EventValidator;
import ru.practicum.validator.UserValidator;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {
    private final RequestRepo requestRepo;
    private final EventRepo eventRepo;
    private final UserRepo userRepo;

    @Override
    @Transactional
    public OutputRequestDto createRequest(Long userId, Long eventId) {
        Event event = eventRepo.findById(eventId)
                .orElseThrow(() -> new NotFoundException("User dont have events with this id"));
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id %d does not exist"));
        if (requestRepo.existsAllByRequester_IdAndEvent_Id(userId, eventId)) {
            throw new RequestCreatedConflictException("User Already create Request on this Event");
        }
        if (event.getInitiator().getId().equals(userId)) {
            throw new RequestCreatedConflictException("Event initiator cant send request on this");
        }
        if (!event.getState().equals(State.PUBLISHED)) {
            throw new RequestCreatedConflictException("You cant create request in not PUBLISHED Event");
        }
        if (event.getParticipantLimit() <= event.getConfirmedRequest() && event.getParticipantLimit() != 0) {
            throw new RequestCreatedConflictException("Event dont have vacancies to apply");
        }
        Request request = Request.builder()
                .created(LocalDateTime.now())
                .event(event)
                .requester(user)
                .status(event.getParticipantLimit() == 0 || !event.getRequestModeration() ?
                        RequestStatus.CONFIRMED : RequestStatus.PENDING)
                .build();
        if (request.getStatus().equals(RequestStatus.CONFIRMED)) {
            event.setConfirmedRequest(event.getConfirmedRequest() + 1);
            eventRepo.save(event);
        }
        return RequestMapper.requestToOutputRequestDto(requestRepo.save(request));
    }

    @Override
    @Transactional(readOnly = true)
    public List<OutputRequestDto> findUserRequests(Long userId) {
        UserValidator.checkUserExist(userRepo, userId);
        return requestRepo.findAllByRequester_Id(userId).stream()
                .map(RequestMapper::requestToOutputRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public OutputRequestDto repealRequest(Long userId, Long requestId) {
        UserValidator.checkUserExist(userRepo, userId);
        Request patchRequest = requestRepo.findAllByRequester_IdAndId(userId, requestId)
                .orElseThrow(() -> new NotFoundException("That request does not exist"));
        patchRequest.setStatus(RequestStatus.CANCELED);
        return RequestMapper.requestToOutputRequestDto(requestRepo.save(patchRequest));
    }

    @Override
    @Transactional(readOnly = true)
    public List<OutputRequestDto> userParticipatesInEvent(Long userId, Long eventId) {
        EventValidator.checkEventExist(eventRepo, eventId);
        UserValidator.checkUserExist(userRepo, userId);
        return requestRepo.findAllByEvent_Id(eventId).stream()
                .map(RequestMapper::requestToOutputRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public OutputUpdateStatusRequestsDto changeRequestStatus(Long userId, Long eventId, InputUpdateStatusRequestDto updateState) {
        UserValidator.checkUserExist(userRepo, userId);
        Event event = eventRepo.findById(eventId)
                .orElseThrow(() -> new NotFoundException("User dont have events with this id"));
        Integer alreadyConfirmed = requestRepo.findConfirmedRequestsOnEvent(eventId).size();
        if (event.getParticipantLimit() <= alreadyConfirmed) {
            throw new RequestCreatedConflictException("Event dont have vacancies to apply");
        }
        List<Request> queryRequests = requestRepo.findAllByIdIn(updateState.getRequestIds());
        if (!queryRequests.stream().allMatch(request -> request.getStatus().equals(RequestStatus.PENDING))) {
            throw new RequestCreatedConflictException("Not all given requests have state PENDING");
        }
        if (updateState.getStatus().equals(RequestStatus.REJECTED)) {
            queryRequests.forEach(request -> request.setStatus(RequestStatus.REJECTED));
            List<Request> saved = requestRepo.saveAll(queryRequests);
            return RequestMapper.requestListToUpdateStateList(saved);
        } else {
            List<Request> result = new ArrayList<>();
            for (int i = 0; i < event.getParticipantLimit() - alreadyConfirmed && i <= queryRequests.size() - 1; i++) {
                Request iteration = queryRequests.get(i);
                iteration.setStatus(RequestStatus.CONFIRMED);
                result.add(iteration);
                queryRequests.remove(queryRequests.get(i));
                event.setConfirmedRequest((long) i + 1);
            }
            eventRepo.save(event);
            result.addAll(queryRequests.stream()
                    .peek(request -> request.setStatus(RequestStatus.REJECTED))
                    .collect(Collectors.toList())
            );
            return RequestMapper.requestListToUpdateStateList(requestRepo.saveAll(result));
        }
    }
}