package ru.practicum.client;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import ru.practicum.dto.InputDto;
import ru.practicum.dto.OutputDto;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Client {
    protected final RestTemplate rest = new RestTemplate();
    private final String path = "http://localhost:9090";

    public void addNewRequest(InputDto inputDto) {
        rest.postForLocation(path + "/hit", inputDto);
    }

    public List<OutputDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        ResponseEntity<OutputDto[]> responseResult = rest.getForEntity(path + "/stats" + "?start=" + start + "&end=" + end +
                "&uris=" + uris + "&unique=" + unique, OutputDto[].class);
        return Arrays.asList(Objects.requireNonNull(responseResult.getBody()));
    }
}