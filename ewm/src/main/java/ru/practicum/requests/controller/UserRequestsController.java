package ru.practicum.requests.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.requests.dto.InputUpdateStatusRequestDto;
import ru.practicum.requests.dto.OutputRequestDto;
import ru.practicum.requests.dto.OutputUpdateStatusRequestsDto;
import ru.practicum.requests.service.RequestService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@Slf4j
public class UserRequestsController {

    private final RequestService requestService;


    @PostMapping("/{userId}/requests")
    @ResponseStatus(HttpStatus.CREATED) //201
    public OutputRequestDto createRequest(@PathVariable Long userId,
                                          @RequestParam() Long eventId) {
        log.trace("REQUEST CONTROLLER: request to create request from user with id: {}", userId);
        return requestService.createRequest(userId, eventId);
    }

    @GetMapping("/{userId}/requests")
    @ResponseStatus(HttpStatus.OK)
    public List<OutputRequestDto> getUserRequest(@PathVariable Long userId) {
        log.trace("REQUEST CONTROLLER: request to find user requests from id: {}", userId);
        return requestService.findUserRequests(userId);
    }

    @PatchMapping("/{userId}/requests/{requestId}/cancel")
    @ResponseStatus(HttpStatus.OK)
    public OutputRequestDto repealRequest(@PathVariable Long userId,
                                          @PathVariable Long requestId) {
        log.trace("REQUEST CONTROLLER: request to repeal user request from id: {}", userId);
        return requestService.repealRequest(userId, requestId);
    }

    @GetMapping("/{userId}/events/{eventId}/requests")
    @ResponseStatus(HttpStatus.OK)
    public List<OutputRequestDto> userParticipatesInEvent(@PathVariable Long userId,
                                                          @PathVariable Long eventId) {
        log.trace("REQUEST CONTROLLER: request to find event were user participated with id: {}", userId);
        return requestService.userParticipatesInEvent(userId, eventId);
    }

    @PatchMapping("/{userId}/events/{eventId}/requests")
    @ResponseStatus(HttpStatus.OK)
    public OutputUpdateStatusRequestsDto changeRequestStatus(@PathVariable Long userId,
                                                             @PathVariable Long eventId,
                                                             @RequestBody(required = false) InputUpdateStatusRequestDto updateState) {
        log.trace("REQUEST CONTROLLER: request to change requests state: {}", updateState);
        return requestService.changeRequestStatus(userId, eventId, updateState);
    }


}
