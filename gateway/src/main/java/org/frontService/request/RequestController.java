package org.frontService.request;

import lombok.RequiredArgsConstructor;
import org.dtoPoint.requests.OutputRequestDto;
import org.springframework.boot.actuate.endpoint.web.annotation.RestControllerEndpoint;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Objects;

@Controller
@RequiredArgsConstructor
public class RequestController {
    private final RequestClient requestClient;

    @GetMapping("/create/request")
    public String createRequest() {
        return "requests/requestCreate";
    }

    @PostMapping("/create/request")
    public String createRequest(@RequestParam Long userId, @RequestParam Long eventId, Model model) {
        try {
            model.addAttribute("dto", requestClient.createRequest(userId, eventId));
        } catch (HttpClientErrorException e) {
            if (Objects.requireNonNull(e.getMessage()).contains("404")) {
                return "/exception/notFoundPage";
            }
        }
        return "requests/requestOutputDto";
    }

    @GetMapping("/find/requests")
    public String findRequests() {
        return "requests/requestFind";
    }

    @GetMapping("/find/requests/user")
    public String findRequestsRest(@RequestParam Long userId) {
        return "redirect:http://localhost:8080/users/" + userId + "/requests";
    }

    @GetMapping("/reject/requests")
    public String rejectRequests() {
        return "requests/requestReject";
    }

    @GetMapping("/reject/requests/rest")
    public String rejectRequestsRest(@RequestParam Long userId, @RequestParam Long eventId, Model model) {
        try {
            model.addAttribute("dto", requestClient.rejectRequest(userId, eventId));
        } catch (HttpClientErrorException e) {
            if (Objects.requireNonNull(e.getMessage()).contains("404")) {
                return "/exception/notFoundPage";
            }
        }
        return "requests/requestOutputDto";
    }
}
