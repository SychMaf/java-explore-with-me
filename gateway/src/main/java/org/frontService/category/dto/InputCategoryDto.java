package org.frontService.category.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class InputCategoryDto {
    @NotBlank
    @Size(max = 50)
    private String name;
}
