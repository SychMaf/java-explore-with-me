package org.frontService.event;

import lombok.RequiredArgsConstructor;
import org.dtoPoint.events.InputEventDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Objects;

@Controller
@RequiredArgsConstructor
public class EventController {
    private final EventClient eventClient;

    @GetMapping("/find/event/param")
    public String findEventsWithParams() {
        return "events/eventFindWithParams";
    }

    @GetMapping("/create/event")
    public String createEvent(@ModelAttribute InputEventDto inputEventDto, Model model) {
        model.addAttribute(inputEventDto);
        return "events/eventCreateForm";
    }

    @PostMapping("/create/event")
    public String createEvent(@RequestParam Long userId, @ModelAttribute InputEventDto inputEventDto, Model model) {
        try {
            model.addAttribute("dto", eventClient.addEvent(inputEventDto, userId));
        } catch (HttpClientErrorException e) {
            if (Objects.requireNonNull(e.getMessage()).contains("Email already used")) {
                return "/exception/emailException";
            }
        }
        return "events/fullEventInformation";
    }

    @GetMapping("/find/event/id")
    public String findEventById() {
        return "/events/eventFindByIdForm";
    }

    @PostMapping("/find/event/id")
    public String findEventById(@RequestParam Long userId,
                                @RequestParam Long eventId,
                                Model model) {
        try {
            model.addAttribute("dto", eventClient.findById(userId, eventId));
        } catch (HttpClientErrorException e) {
            if (Objects.requireNonNull(e.getMessage()).contains("Email already used")) {
                return "/exception/emailException";
            }
        }
        return "events/fullEventInformation";
    }
}
