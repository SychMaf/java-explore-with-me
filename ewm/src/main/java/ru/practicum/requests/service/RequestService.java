package ru.practicum.requests.service;

import ru.practicum.requests.dto.OutputRequestDto;

import java.util.List;

public interface RequestService {
    OutputRequestDto createRequest(Long userId, Long eventId);

    List<OutputRequestDto> findUserRequests(Long userId);

    OutputRequestDto repealRequest(Long userId, Long requestId);
}
