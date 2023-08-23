package ru.practicum.requests.service;

import ru.practicum.requests.dto.InputUpdateStatusRequestDto;
import ru.practicum.requests.dto.OutputRequestDto;
import ru.practicum.requests.dto.OutputUpdateStatusRequestsDto;

import java.util.List;

public interface RequestService {
    OutputRequestDto createRequest(Long userId, Long eventId);

    List<OutputRequestDto> findUserRequests(Long userId);

    OutputRequestDto repealRequest(Long userId, Long requestId);

    List<OutputRequestDto> userParticipatesInEvent(Long userId, Long eventId);

    OutputUpdateStatusRequestsDto changeRequestStatus(Long userId, Long eventId, InputUpdateStatusRequestDto updateState);
}