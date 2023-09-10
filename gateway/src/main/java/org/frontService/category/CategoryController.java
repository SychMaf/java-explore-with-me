package org.frontService.category;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CategoryController {

    @GetMapping("/findCategoryParams")
    public String findUsers() {
        return "category/findCategoryParams";
    }
}
