package org.frontService.event;

import lombok.RequiredArgsConstructor;
import org.dtoPoint.events.InputEventDto;
import org.dtoPoint.events.UpdateEventDto;
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

    @GetMapping("/find/event/user/id")
    public String findEventByIdAndUser() {
        return "events/eventFindByIdAndUserForm";
    }

    @PostMapping("/find/event/user/id")
    public String findEventByIdAndUser(@RequestParam Long userId,
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

    @GetMapping("/find/event/user")
    public String findEventByUser() {
        return "/events/eventFindByUserForm";
    }

    @PostMapping("/find/event/user")
    public String findEventByUser(@RequestParam Long userId) {
        return "redirect:http://localhost:8080/users/" + userId + "/events";
    }

    @GetMapping("/find/event/id")
    public String findEventById() {
        return "/events/eventFindByIdForm";
    }

    @PostMapping("/find/event/id")
    public String findEventById(@RequestParam Long eventId) {
        return "redirect:http://localhost:8080/events/" + eventId;
    }

    @GetMapping("/patch/event")
    public String patchEvent(@ModelAttribute UpdateEventDto updateEventDto, Model model) {
        model.addAttribute(updateEventDto);
        return "events/eventPatchForm";
    }

    @PostMapping("/patch/event")
    public String patchEvent(@ModelAttribute UpdateEventDto updateEventDto,
                             @RequestParam Long userId,
                             @RequestParam Long eventId,
                             Model model) {
        try {
            model.addAttribute("dto", eventClient.patchEvent(updateEventDto, userId, eventId));
        } catch (HttpClientErrorException e) {
            if (Objects.requireNonNull(e.getMessage()).contains("Email already used")) {
                return "/exception/emailException";
            }
        }
        return "events/fullEventInformation";
    }

    @GetMapping("/patch/event/admin")
    public String patchEventAdmin(@ModelAttribute UpdateEventDto updateEventDto, Model model) {
        model.addAttribute(updateEventDto);
        return "events/eventAdminPatchForm";
    }

    @PostMapping("/patch/event/admin")
    public String patchEventAdmin(@ModelAttribute UpdateEventDto updateEventDto,
                             @RequestParam Long eventId,
                             Model model) {
        try {
            model.addAttribute("dto", eventClient.patchEventAdmin(updateEventDto, eventId));
        } catch (HttpClientErrorException e) {
            if (Objects.requireNonNull(e.getMessage()).contains("Email already used")) {
                return "/exception/emailException";
            }
        }
        return "events/fullEventInformation";
    }

    @GetMapping("/find/event/param/admin")
    public String findEventsWithParamsAdmin() {
        return "events/eventAdminFindWithParams";
    }
}
