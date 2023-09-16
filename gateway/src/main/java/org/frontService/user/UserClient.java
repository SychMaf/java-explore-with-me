package org.frontService.user;

import org.frontService.client.BaseClient;
import org.dtoPoint.user.InputUserDto;
import org.dtoPoint.user.OutputUserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;

@Service
public class UserClient extends BaseClient {

    @Autowired
    public UserClient(RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory("http://localhost:8080"))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public OutputUserDto saveUser(InputUserDto userDto) {
        return post("/admin/users", userDto, OutputUserDto.class).getBody();
    }

    public void deleteUser(long id) {
        delete("/admin/users/" + id, null);
    }
}
