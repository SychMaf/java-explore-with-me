package org.frontService.category;

import org.frontService.category.dto.InputCategoryDto;
import org.frontService.category.dto.OutputCategoryDto;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Controller
public class CategoryController {
    protected final RestTemplate rest = new RestTemplate();
    private String path = "http://localhost:8080";

    @GetMapping("/findCategoryParams")
    public String findUsers() {
        return "category/findCategoryParams";
    }

    @PostMapping("/createCategory")
    public String createCategory(@ModelAttribute InputCategoryDto inputCategoryDto, Model model) {
        try {
            ResponseEntity<OutputCategoryDto> dto = rest.postForEntity(path + "/admin/categories", new HttpEntity<>(inputCategoryDto), OutputCategoryDto.class);
            model.addAttribute("dto", Objects.requireNonNull(dto.getBody()));
        } catch (HttpClientErrorException e) {
            if (Objects.requireNonNull(e.getMessage()).contains("Name already exist")) {
                return "/exception/nameException";
            }
        }
        return "category/categoryCreate";
    }

    @GetMapping("/findCategoryByIdForm")
    public String findByIdCategory(Model model) {
        return "category/categoryFindIdForm";
    }

    @PostMapping("/categoryFindById")
    public String completedFindById(@RequestParam Long catId, Model model) {
        try {
            ResponseEntity<OutputCategoryDto> dto = rest.getForEntity(path + "/categories/" + catId, OutputCategoryDto.class);
            model.addAttribute("dto", Objects.requireNonNull(dto.getBody()));
        } catch (HttpClientErrorException e) {
            if (Objects.requireNonNull(e.getMessage()).contains("404")) {
                return "/exception/notFoundPage";
            }
        }
        return "category/categoryFindId";
    }

}
