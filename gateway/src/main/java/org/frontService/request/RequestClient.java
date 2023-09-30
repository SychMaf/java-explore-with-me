package org.frontService.request;


import org.dtoPoint.requests.OutputRequestDto;
import org.frontService.client.BaseClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;

@Service
public class RequestClient extends BaseClient {

    @Autowired
    public RequestClient(RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory("http://localhost:8080"))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public OutputRequestDto createRequest(Long userId, Long eventId) {
        return post("/users/" + userId + "/requests?eventId=" + eventId, null, OutputRequestDto.class).getBody();
    }

    public OutputRequestDto rejectRequest(Long userId, Long eventId) {
        return patch("users/" + userId + "/requests/" + eventId + "/cancel", null, OutputRequestDto.class).getBody();
    }
}
