package org.frontService.category.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OutputCategoryDto {
    private Long id;
    private String name;

    @Override
    public String toString() {
        return "OutputCategoryDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
