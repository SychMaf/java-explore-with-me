package ru.practicum.requests.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.requests.model.Request;
import ru.practicum.requests.model.RequestStatus;

import java.util.List;
import java.util.stream.Collectors;

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

    public OutputUpdateStatusRequestsDto requestListToUpdateStateList(List<Request> requestList) {
        return OutputUpdateStatusRequestsDto.builder()
                .confirmedRequests(requestList.stream()
                        .filter(request -> request.getStatus().equals(RequestStatus.CONFIRMED))
                        .map(RequestMapper::requestToOutputRequestDto)
                        .collect(Collectors.toList()))
                .rejectedRequests(requestList.stream()
                        .filter(request -> request.getStatus().equals(RequestStatus.REJECTED))
                        .map(RequestMapper::requestToOutputRequestDto)
                        .collect(Collectors.toList()))
                .build();


    }
}
