package org.frontService.category;

import lombok.RequiredArgsConstructor;
import org.dtoPoint.category.InputCategoryDto;
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
public class CategoryController {
    private final CategoryClient categoryClient;

    @GetMapping("/find/category/params")
    public String findCategory() {
        return "category/findCategoryParams";
    }

    @GetMapping("/create/category")
    public String createCategoryForm(@ModelAttribute InputCategoryDto inputCategoryDto, Model model) {
        model.addAttribute(inputCategoryDto);
        return "category/categoryCreateForm";
    }

    @PostMapping("/create/category")
    public String createCategory(@ModelAttribute InputCategoryDto inputCategoryDto, Model model) {
        try {
            model.addAttribute("dto", categoryClient.saveCategory(inputCategoryDto));
        } catch (HttpClientErrorException e) {
            if (Objects.requireNonNull(e.getMessage()).contains("Name already exist")) {
                return "/exception/nameException";
            }
        }
        return "category/categoryCreate";
    }

    @GetMapping("/find/category/id")
    public String findByIdCategory() {
        return "category/categoryFindIdForm";
    }

    @PostMapping("/find/category/id")
    public String findByIdCategory(@RequestParam Long catId, Model model) {
        try {
            model.addAttribute("dto", categoryClient.getCategoryById(catId));
        } catch (HttpClientErrorException e) {
            if (Objects.requireNonNull(e.getMessage()).contains("404")) {
                return "/exception/notFoundPage";
            }
        }
        return "category/categoryFindId";
    }

    @GetMapping("/delete/category")
    public String deleteCategory() {
        return "category/categoryDeleteForm";
    }

    @PostMapping("/delete/category")
    public String deleteCategory(@RequestParam Long catId) {
        categoryClient.deleteCategory(catId);
        return "category/categoryDelete";
    }

    @GetMapping("/update/category")
    public String patchCategory(@ModelAttribute InputCategoryDto inputCategoryDto, Model model) {
        model.addAttribute(inputCategoryDto);
        return "category/categoryPatchForm";
    }

    @PostMapping("/update/category")
    public String patchCategory(@ModelAttribute InputCategoryDto inputCategoryDto, @RequestParam Long catId, Model model) {
        try {
            model.addAttribute("dto", categoryClient.patchCategory(inputCategoryDto, catId));
        } catch (HttpClientErrorException e) {
            if (Objects.requireNonNull(e.getMessage()).contains("Name already exist")) {
                return "/exception/nameException";
            }
        }
        return "category/categoryPatch";
    }

}
