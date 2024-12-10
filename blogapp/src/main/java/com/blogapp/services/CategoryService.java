package com.blogapp.services;

import com.blogapp.payloads.CategoryDto;
import org.springframework.stereotype.Service;

import java.util.List;

public interface CategoryService {
    // create
     CategoryDto createCategory(CategoryDto categoryDto);
    //update
     CategoryDto updateCategory(CategoryDto categoryDto, Integer categoryId);


    //delete
    void deleteCategory(Integer categoryId);
    //get
    CategoryDto getCategory( Integer categoryId);
    //getAll
    List<CategoryDto> getAllCategories();

}
