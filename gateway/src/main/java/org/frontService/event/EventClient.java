package org.frontService.event;

import org.dtoPoint.category.InputCategoryDto;
import org.dtoPoint.category.OutputCategoryDto;
import org.dtoPoint.events.FullOutputEventDto;
import org.dtoPoint.events.InputEventDto;
import org.frontService.client.BaseClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;

@Service
public class EventClient extends BaseClient {
    @Autowired
    public EventClient(RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory("http://localhost:8080"))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public FullOutputEventDto addEvent(InputEventDto eventDto, Long userId) {
        return post("/users/"+ userId + "/events", eventDto, FullOutputEventDto.class).getBody();
    }

    public FullOutputEventDto findById(Long userId, Long eventId) {
        return get("/users/" + userId + "/events/" + eventId, FullOutputEventDto.class).getBody();
    }
}
