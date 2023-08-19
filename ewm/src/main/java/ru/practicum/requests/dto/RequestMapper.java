package ru.practicum.requests.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.requests.model.Request;

@UtilityClass
public class RequestMapper {
    public OutputRequestDto requestToOutputRequestDto(Request request) {
        return OutputRequestDto.builder()
                .created(request.getCreated())
                .event(request.getEvent().getId())
                .id(request.getId())
                .requester(request.getRequester().getId())
                .status(request.getStatus())
                .build();
    }
}
