package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.InputDto;
import ru.practicum.dto.OutputDto;
import ru.practicum.mapper.StatDtoMapper;
import ru.practicum.model.Stats;
import ru.practicum.repo.StatRepo;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatServiceImpl implements StatService {
    private final StatRepo statRepo;

    @Override
    @Transactional
    public void addNewRequest(InputDto inputDto) {
        Stats stats = StatDtoMapper.toStatFromDto(inputDto);
        statRepo.save(stats);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OutputDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        uris = uris.stream()
                .map(uri -> (uri.replace("[", "").replace("]", "")))
                .collect(Collectors.toList());
        if (unique) {
            if (uris.isEmpty()) {
                return statRepo.findWithoutUrisAndUniqueIp(start, end);
            } else {
                return statRepo.findWithUrisAndUniqueIp(start, end, uris);
            }
        } else {
            if (uris.isEmpty()) {
                return statRepo.findAllWithoutUrisAndNotUniqueIp(start, end);
            } else {
                return statRepo.findAllWithUrisAndNotUniqueIp(start, end, uris);
            }
        }
    }
}
