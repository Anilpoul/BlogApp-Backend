package com.blogapp.services.impl;

import com.blogapp.exceptions.ResourceNotFoundException;
import com.blogapp.models.Category;
import com.blogapp.models.Post;
import com.blogapp.models.User;
import com.blogapp.payloads.PostDto;
import com.blogapp.payloads.PostResponse;
import com.blogapp.payloads.UserResponseDto;
import com.blogapp.repositories.CategoryRepo;
import com.blogapp.repositories.PostRepo;
import com.blogapp.repositories.UserRepo;
import com.blogapp.services.PostService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostRepo postRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private CategoryRepo categoryRepo;

    @Value("${app.image.base-url}")
    private String imageBaseUrl;

    private PostDto convertToDtoWithImageUrl(Post post) {
        PostDto postDto = modelMapper.map(post, PostDto.class);
        postDto.setImageUrl(imageBaseUrl + post.getImageName());

        UserResponseDto userDto = new UserResponseDto();
        userDto.setId(post.getUser().getId());
        userDto.setName(post.getUser().getName());
        postDto.setUser(userDto);

        return postDto;
    }

    @Override
    public PostDto createPost(PostDto postDto, Integer userId, Integer categoryId) {
        User user = this.userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "userId", userId));

        Category category = this.categoryRepo.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));

        Post post = this.modelMapper.map(postDto, Post.class);

        if (post.getImageName() == null || post.getImageName().isEmpty()) {
            post.setImageName("default.png");
        }
        
        post.setCreationDate(new Date());
        post.setUser(user);
        post.setCategory(category);

        Post newPost = this.postRepo.save(post);
        return convertToDtoWithImageUrl(newPost);
    }

    @Override
    public PostDto updatePost(PostDto postDto, Integer postId) {
        Post post = this.postRepo.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "postId", postId));

        post.setPostTitle(postDto.getPostTitle());
        post.setContent(postDto.getContent());

        Post updatedPost = this.postRepo.save(post);
        return convertToDtoWithImageUrl(updatedPost);
    }

    @Override
    public void deletePost(Integer postId) {
        Post post = this.postRepo.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "postId", postId));
        this.postRepo.delete(post);
    }

    @Override
    public PostResponse getAllPost(Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        Page<Post> pagePost = this.postRepo.findAll(pageable);
        List<Post> posts = pagePost.getContent();

        List<PostDto> postDtos = posts.stream()
                .map(this::convertToDtoWithImageUrl)
                .collect(Collectors.toList());

        PostResponse postResponse = new PostResponse();
        postResponse.setContent(postDtos);
        postResponse.setPageNumber(pagePost.getNumber());
        postResponse.setPageSize(pagePost.getSize());
        postResponse.setTotalPages(pagePost.getTotalPages());
        postResponse.setTotalElements(pagePost.getTotalElements());
        postResponse.setLastpage(pagePost.isLast());

        return postResponse;
    }

    @Override
    public PostDto getPostById(Integer postId) {
        Post post = this.postRepo.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "postId", postId));
        return convertToDtoWithImageUrl(post);
    }

    @Override
    public PostResponse getPostsByCategory(Integer categoryId, Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        Category category = this.categoryRepo.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));

        Page<Post> pagePosts = this.postRepo.findByCategory(category, pageable);
        List<Post> posts = pagePosts.getContent();

        List<PostDto> postDtos = posts.stream()
                .map(this::convertToDtoWithImageUrl)
                .collect(Collectors.toList());

        PostResponse postResponse = new PostResponse();
        postResponse.setContent(postDtos);
        postResponse.setPageNumber(pagePosts.getNumber());
        postResponse.setPageSize(pagePosts.getSize());
        postResponse.setTotalPages(pagePosts.getTotalPages());
        postResponse.setTotalElements(pagePosts.getTotalElements());
        postResponse.setLastpage(pagePosts.isLast());

        return postResponse;
    }

    @Override
    public List<PostDto> getPostsByUser(Integer userId) {
        User user = this.userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "userId", userId));
        List<Post> posts = this.postRepo.findByUser(user);

        return posts.stream()
                .map(this::convertToDtoWithImageUrl)
                .collect(Collectors.toList());
    }

    @Override
    public Page<PostDto> searchPost(String keyword, int pageNumber, int pageSize) {
        if (keyword == null || keyword.trim().isEmpty()) {
            throw new IllegalArgumentException("Keyword must not be empty");
        }

        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("creationDate").descending());

        Page<Post> postPage = postRepo.findByPostTitleContainingIgnoreCaseOrContentContainingIgnoreCase(
                keyword.trim(), keyword.trim(), pageable
        );

        return postPage.map(this::convertToDtoWithImageUrl);
    }
}
