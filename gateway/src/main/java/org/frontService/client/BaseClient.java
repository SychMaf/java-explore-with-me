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

    protected <T, S> ResponseEntity<S> post(String path, T body, Class<S> responseClass) {
        return post(path, null, body, responseClass);
    }

    protected <T, S> ResponseEntity<S> post(String path, @Nullable Map<String, Object> parameters, T body, Class<S> responseClass) {
        return makeAndSendRequest(HttpMethod.POST, path, parameters, body, responseClass);
    }

    protected <T, S> ResponseEntity<S> patch(String path, T body, Class<S> responseClass) {
        return patch(path, null, body, responseClass);
    }

    protected <T, S> ResponseEntity<S> patch(String path, @Nullable Map<String, Object> parameters, T body, Class<S> responseClass) {
        return makeAndSendRequest(HttpMethod.PATCH, path, parameters, body, responseClass);
    }

    protected <S> ResponseEntity<S> get(String path, Class<S> responseClass) {
        return get(path, null, responseClass);
    }

    protected <S> ResponseEntity<S> get(String path, @Nullable Map<String, Object> parameters, Class<S> responseClass) {
        return makeAndSendRequest(HttpMethod.GET, path, parameters, null, responseClass);
    }

    protected <S> ResponseEntity<S> delete(String path, Class<S> responseClass) {
        return delete(path, null, responseClass);
    }

    protected <S> ResponseEntity<S> delete(String path, @Nullable Map<String, Object> parameters, Class<S> responseClass) {
        return makeAndSendRequest(HttpMethod.DELETE, path, parameters, null, responseClass);
    }


    private <T, S> ResponseEntity<S> makeAndSendRequest(HttpMethod method, String path, @Nullable Map<String, Object> parameters, @Nullable T body, Class<S> responseClass) {
        HttpEntity<T> requestEntity = new HttpEntity<>(body, defaultHeaders());

        ResponseEntity<S> exploreServerResponse;
        try {
            if (parameters != null) {
                exploreServerResponse = rest.exchange(path, method, requestEntity, responseClass, parameters);
            } else {
                exploreServerResponse = rest.exchange(path, method, requestEntity, responseClass);
            }
        } catch (HttpStatusCodeException e) {
            throw new RuntimeException(e);
        }
        return prepareGatewayResponse(exploreServerResponse);
    }

    private HttpHeaders defaultHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        return headers;
    }

    private <S> ResponseEntity<S> prepareGatewayResponse(ResponseEntity<S> response) {
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
