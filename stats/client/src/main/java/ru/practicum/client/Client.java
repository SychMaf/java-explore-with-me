package ru.practicum.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.practicum.dto.InputDto;
import ru.practicum.dto.OutputDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
public class Client {
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    protected final RestTemplate rest = new RestTemplate();
    @Value("${stats-server.url}")
    private String path;

    public void addNewRequest(InputDto inputDto) {
        rest.postForLocation(path + "/hit", inputDto);
    }

    public List<OutputDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {

        ResponseEntity<OutputDto[]> responseResult = rest.getForEntity(path + "/stats" +
                "?start=" + start.format(dateTimeFormatter) +
                "&end=" + end.format(dateTimeFormatter) +
                "&uris=" + uris +
                "&unique=" + unique, OutputDto[].class);
        return Arrays.asList(Objects.requireNonNull(responseResult.getBody()));
    }
}