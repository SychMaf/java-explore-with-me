package ru.practicum.requests.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.events.model.Event;
import ru.practicum.events.repo.EventRepo;
import ru.practicum.exception.exceptions.NotFoundException;
import ru.practicum.requests.dto.InputUpdateStatusRequestDto;
import ru.practicum.requests.dto.OutputRequestDto;
import ru.practicum.requests.dto.OutputUpdateStatusRequestsDto;
import ru.practicum.requests.dto.RequestMapper;
import ru.practicum.requests.model.Request;
import ru.practicum.requests.model.RequestStatus;
import ru.practicum.requests.repo.RequestRepo;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepo;

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
        Request request = Request.builder()
                .created(LocalDateTime.now())
                .event(event)
                .requester(user)
                .status(event.getParticipantLimit() == 0 ?
                        RequestStatus.CONFIRMED : RequestStatus.PENDING)
                .build();
        return RequestMapper.requestToOutputRequestDto(requestRepo.save(request));
    }

    @Override
    @Transactional(readOnly = true)
    public List<OutputRequestDto> findUserRequests(Long userId) {
        if (!userRepo.existsById(userId)) {
            throw new NotFoundException("User with id %d does not exist");
        }
        List<Request> requests = requestRepo.findAllByRequester_Id(userId);
        return requests.stream()
                .map(RequestMapper::requestToOutputRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public OutputRequestDto repealRequest(Long userId, Long requestId) {
        if (!userRepo.existsById(userId)) {
            throw new NotFoundException("User with id %d does not exist");
        }
        Request patchRequest = requestRepo.findAllByRequester_IdAndId(userId, requestId)
                .orElseThrow(() -> new NotFoundException("User with id %d does not exist"));
        patchRequest.setStatus(RequestStatus.CANCELED);
        return RequestMapper.requestToOutputRequestDto(requestRepo.save(patchRequest));
    }

    @Override
    @Transactional
    public List<OutputRequestDto> userParticipatesInEvent(Long userId, Long eventId) {
        Event event = eventRepo.findById(eventId)
                .orElseThrow(() -> new NotFoundException("User dont have events with this id"));
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id %d does not exist"));
        Long testUserId = user.getId();
        List<Request> all = requestRepo.findAll();
        List<Request> requests = requestRepo.findAllByEvent(event);
        return requests.stream()
                .map(RequestMapper::requestToOutputRequestDto)
                .collect(Collectors.toList());
    }

    //ЭТО НАДО БУДЕТ ЖБ УЛУЧШИТЬ
    @Override
    @Transactional
    public OutputUpdateStatusRequestsDto changeRequestStatus(Long userId, Long eventId, InputUpdateStatusRequestDto updateState) {
        if (!userRepo.existsById(userId)) {
            throw new NotFoundException("User with id %d does not exist");
        }
        Event event = eventRepo.findById(eventId)
                .orElseThrow(() -> new NotFoundException("User dont have events with this id"));
        List<Request> queryRequests = requestRepo.findAllByIdIn(updateState.getRequestIds());
        if (!queryRequests.stream().allMatch(request -> request.getStatus().equals(RequestStatus.PENDING))) {
            throw new RuntimeException("Not all given requests have state PENDING");
        }
        Integer limit = event.getParticipantLimit();
        Integer alreadyConfirmed = requestRepo.findConfirmedRequestsOnEvent(eventId).size();
        if (limit <= alreadyConfirmed) {
            throw new RuntimeException("full");
        }
        if (updateState.getStatus().equals(RequestStatus.REJECTED)) {
            queryRequests.forEach(request -> request.setStatus(RequestStatus.REJECTED));
            List<Request> saved = requestRepo.saveAll(queryRequests);
            return RequestMapper.requestListToUpdateStateList(saved);
        } else {
            List<Request> result = new ArrayList<>();
            for (int i = 0; i < limit-alreadyConfirmed && i <= queryRequests.size() - 1; i++) {
                Request iteration = queryRequests.get(i);
                iteration.setStatus(RequestStatus.CONFIRMED);
                result.add(iteration);
                queryRequests.remove(queryRequests.get(i));
                event.setConfirmedRequest((long) i+1);
            }
            result.addAll(queryRequests.stream()
                    .peek(request -> request.setStatus(RequestStatus.REJECTED))
                    .collect(Collectors.toList())
            );
            List<Request> saved = requestRepo.saveAll(result);
            eventRepo.save(event);
            return RequestMapper.requestListToUpdateStateList(saved);
        }
    }
}
