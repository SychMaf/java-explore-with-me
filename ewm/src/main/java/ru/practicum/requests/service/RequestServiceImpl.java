package ru.practicum.requests.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.events.model.Event;
import ru.practicum.events.repo.EventRepo;
import ru.practicum.exception.exceptions.NotFoundException;
import ru.practicum.requests.dto.OutputRequestDto;
import ru.practicum.requests.dto.RequestMapper;
import ru.practicum.requests.model.Request;
import ru.practicum.requests.model.RequestStatus;
import ru.practicum.requests.repo.RequestRepo;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepo;

import java.time.LocalDateTime;
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
                .status(RequestStatus.PENDING)
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
}
