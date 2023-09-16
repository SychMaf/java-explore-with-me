package org.frontService.event;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class EventController {

    @GetMapping("/findEvents")
    public String findEventsWithParams() {
        return "events/eventFindWithParams";
    }
}
