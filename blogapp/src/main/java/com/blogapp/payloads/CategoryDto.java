package com.blogapp.payloads;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class CategoryDto {
    private Integer categoryId;

    @NotEmpty
    @Size(min = 4, message = "min 4 characters needed")
    private String categoryTitle;

    @NotEmpty
    @Size(min = 10, message = " Min 10 Characters needed")
    private String categoryDescription;
}
