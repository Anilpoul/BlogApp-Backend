package com.blogapp.services.impl;

import com.blogapp.exceptions.ResourceNotFoundException;
import com.blogapp.models.Category;
import com.blogapp.payloads.CategoryDto;
import com.blogapp.repositories.CategoryRepo;
import com.blogapp.services.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepo categoryRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CategoryDto createCategory(CategoryDto categoryDto) {
        Category cat = this.modelMapper.map(categoryDto, Category.class);
        Category addedCategory = this.categoryRepo.save(cat);
        return this.modelMapper.map(addedCategory, CategoryDto.class);
    }

    @Override
    public CategoryDto updateCategory(CategoryDto categoryDto, Integer categoryId) {
        Category cat = this.categoryRepo.findById(categoryId)
                .orElseThrow(()-> new ResourceNotFoundException("category "," category Id ", categoryId ));

        cat.setCategoryTitle(categoryDto.getCategoryTitle());
        cat.setCategoryDescription(categoryDto.getCategoryDescription());

        Category updatedCategory = this.categoryRepo.save(cat);
        return this.modelMapper.map(updatedCategory, categoryDto.getClass());
    }

    @Override
    public void deleteCategory(Integer categoryId) {
        Category cat = this.categoryRepo.findById(categoryId)
                .orElseThrow(()-> new ResourceNotFoundException("category "," category Id ", categoryId ));
        this.categoryRepo.delete(cat);
    }

    @Override
    public CategoryDto getCategory(Integer categoryId) {
        Category cat = this.categoryRepo.findById(categoryId)
                .orElseThrow(()-> new ResourceNotFoundException("category "," category Id ", categoryId ));
        return this.modelMapper.map(cat,CategoryDto.class);
    }

    @Override
    public List<CategoryDto> getAllCategories() {
        List<Category> categories = this.categoryRepo.findAll();
        return categories.stream().map((cat)-> this.modelMapper.map(cat, CategoryDto.class)).collect(Collectors.toList());
    }
}
