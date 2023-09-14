package org.frontService.client;

import org.springframework.http.*;
import org.springframework.lang.Nullable;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

public class BaseClient {
    protected final RestTemplate rest;

    public BaseClient(RestTemplate rest) {
        this.rest = rest;
    }

    protected <T> ResponseEntity<Object> post(String path, T body, Class responseClass) {
        return post(path, null, body, responseClass);
    }

    protected <T> ResponseEntity<Object> post(String path, @Nullable Map<String, Object> parameters, T body, Class responseClass) {
        return makeAndSendRequest(HttpMethod.POST, path, parameters, body, responseClass);
    }

//    protected ResponseEntity<Object> get(String path) {
//        return get(path, null, null);
//    }
//
//    protected ResponseEntity<Object> get(String path, long userId) {
//        return get(path, userId, null);
//    }
//
//    protected ResponseEntity<Object> get(String path, Long userId, @Nullable Map<String, Object> parameters) {
//        return makeAndSendRequest(HttpMethod.GET, path, parameters, null);
//    }

    private <T> ResponseEntity<Object> makeAndSendRequest(HttpMethod method, String path, @Nullable Map<String, Object> parameters, @Nullable T body, Class responseClass) {
        HttpEntity<T> requestEntity = new HttpEntity<>(body, defaultHeaders());

        ResponseEntity<Object> exploreServerResponse;
        try {
            if (parameters != null) {
                exploreServerResponse = rest.exchange(path, method, requestEntity, responseClass, parameters);
            } else {
                exploreServerResponse = rest.exchange(path, method, requestEntity, responseClass);
            }
        } catch (HttpStatusCodeException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsByteArray());
        }
        return prepareGatewayResponse(exploreServerResponse);
    }

    private HttpHeaders defaultHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        return headers;
    }

    private static ResponseEntity<Object> prepareGatewayResponse(ResponseEntity<Object> response) {
        if (response.getStatusCode().is2xxSuccessful()) {
            return response;
        }

        ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.status(response.getStatusCode());

        if (response.hasBody()) {
            return responseBuilder.body(response.getBody());
        }

        return responseBuilder.build();
    }
}
