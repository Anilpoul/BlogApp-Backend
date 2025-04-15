package com.blogapp.services;

import com.blogapp.models.Post;
import com.blogapp.payloads.PostDto;
import com.blogapp.payloads.PostResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface PostService {
    //create
    PostDto createPost(PostDto postDto, Integer userId, Integer categoryId);
    //update
    PostDto updatePost(PostDto postDto, Integer postId);

    //delete
    void deletePost(Integer postId);

    // get all posts
    PostResponse getAllPost(Integer pageNumber, Integer pageSize, String sortBy, String sortDir);

    //get single post
    PostDto getPostById(Integer postId);

    // get all posts by category
    List<PostDto> getPostsByCategory(Integer categoryId);

    //get all Posts by User
    List<PostDto> getPostsByUser(Integer userId);

    // Search with title and content
    Page<PostDto> searchPost(String keyword, int page, int size);



}
