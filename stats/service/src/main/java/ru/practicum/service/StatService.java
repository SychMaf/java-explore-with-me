package ru.practicum.service;

import ru.practicum.dto.InputDto;
import ru.practicum.dto.OutputDto;

import java.time.LocalDateTime;
import java.util.List;

public interface StatService {
    void addNewRequest(InputDto inputDto);

    List<OutputDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique);
}
