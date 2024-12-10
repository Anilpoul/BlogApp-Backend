package com.blogapp.controllers;

import com.blogapp.payloads.ApiResponse;
import com.blogapp.payloads.CategoryDto;
import com.blogapp.services.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@CrossOrigin(origins = "http://127.0.0.1:5500")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    //create
    @PostMapping("/")
    public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CategoryDto categoryDto){
        CategoryDto createdCategory = this.categoryService.createCategory(categoryDto);
        return new ResponseEntity<>(createdCategory, HttpStatus.CREATED);
    }
    // update
    @PutMapping("/{categoryId}")
    public ResponseEntity<CategoryDto> updateCategory(@Valid @RequestBody CategoryDto categoryDto, @PathVariable Integer categoryId){
        CategoryDto updatedUser = this.categoryService.updateCategory(categoryDto,categoryId);
        return ResponseEntity.ok(updatedUser);
    }
    //delete
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<ApiResponse> deleteCategory(@PathVariable Integer categoryId){
        this.categoryService.deleteCategory(categoryId);
        return new ResponseEntity(new ApiResponse("Category Deleted Successfully! ", true),HttpStatus.OK);
    }
    //get
    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryDto> getCategory(@PathVariable Integer categoryId){
        return ResponseEntity.ok(this.categoryService.getCategory(categoryId));
    }
    //getAll Categories
    @GetMapping("/")
    public ResponseEntity<List<CategoryDto>> getAllCategories(){
        return ResponseEntity.ok(this.categoryService.getAllCategories());
    }


}
