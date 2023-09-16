package org.frontService.category;

import org.dtoPoint.category.InputCategoryDto;
import org.dtoPoint.category.OutputCategoryDto;
import org.frontService.client.BaseClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;

@Service
public class CategoryClient extends BaseClient {

    @Autowired
    public CategoryClient(RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory("http://localhost:8080"))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public OutputCategoryDto saveCategory(InputCategoryDto categoryDto) {
        return post("/admin/categories", categoryDto, OutputCategoryDto.class).getBody();
    }

    public OutputCategoryDto getCategoryById(Long id) {
        return get("/categories/" + id, OutputCategoryDto.class).getBody();
    }

    public void deleteCategory(Long catId) {
        delete("/admin/categories/" + catId, null);
    }

    public OutputCategoryDto patchCategory(InputCategoryDto inputCategoryDto, Long catId) {
        return patch("/admin/categories/" + catId, inputCategoryDto, OutputCategoryDto.class).getBody();
    }
}
