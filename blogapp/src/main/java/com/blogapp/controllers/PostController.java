package com.blogapp.controllers;

import com.blogapp.config.AppConstants;
import com.blogapp.models.User;
import com.blogapp.payloads.ApiResponse;
import com.blogapp.payloads.PostDto;
import com.blogapp.payloads.PostResponse;
import com.blogapp.repositories.UserRepo;
import com.blogapp.services.FileService;
import com.blogapp.services.PostService;
import com.blogapp.services.UserService;
import com.blogapp.utils.JwtUtil;  // Assuming you have JwtUtil for handling JWT operations
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/api")
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private FileService fileService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private JwtUtil jwtUtil;  // Inject JwtUtil for token handling

    @Value("${project.image}")
    private String path;

    @PostMapping("/user/{userId}/category/{categoryId}/posts")
    public ResponseEntity<PostDto> createPost(
            @RequestParam("postTitle") String postTitle,
            @RequestParam("content") String content,
            @RequestParam(value = "image", required = false) MultipartFile image,
            @PathVariable Integer userId,
            @PathVariable Integer categoryId,
            @RequestHeader("Authorization") String authorizationHeader) throws IOException {

        String token = authorizationHeader.replace("Bearer ", "");

        if (jwtUtil.isTokenExpired(token)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

        String username = jwtUtil.extractUsername(token);

        // ✅ Load UserDetails
        UserDetails userDetails = userService.loadUserByUsername(username);

        if (!jwtUtil.validateToken(token, userDetails)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

        // ✅ Load actual User entity to access userId
        User actualUser = userRepo.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (!actualUser.getId().equals(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

        PostDto postDto = new PostDto();
        postDto.setPostTitle(postTitle);
        postDto.setContent(content);

        if (image != null && !image.isEmpty()) {
            String imageName = this.fileService.uploadImage(path, image);
            postDto.setImageName(imageName);
        } else {
            postDto.setImageName("default.png");
        }

        PostDto createdPostDto = postService.createPost(postDto, userId, categoryId);
        return new ResponseEntity<>(createdPostDto, HttpStatus.CREATED);
    }




    // Get Posts By Category
    @GetMapping("/category/{categoryId}/posts")
    public ResponseEntity<PostResponse> getPostByCategory(
            @PathVariable Integer categoryId,
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) Integer pageSize,
            @RequestParam(value = "sortBy", defaultValue = "postId", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir
    ) {
        PostResponse response = this.postService.getPostsByCategory(categoryId, pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    // Get Posts By User
    @GetMapping("/user/{userId}/posts")
    public ResponseEntity<List<PostDto>> getPostsByUser(@PathVariable Integer userId){
        List<PostDto> posts = this.postService.getPostsByUser(userId);
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    // Get All Posts
    @GetMapping("/posts")
    public ResponseEntity<PostResponse> getAllPosts(
            @RequestParam(value = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.SORT_DIR, required = false) String sortDir) {
        return new ResponseEntity<>(this.postService.getAllPost(pageNumber, pageSize, sortBy, sortDir), HttpStatus.OK);
    }

    // Get Post By PostId
    @GetMapping("/posts/{postId}")
    public ResponseEntity<PostDto> getPostById(@PathVariable Integer postId){
        return new ResponseEntity<>(this.postService.getPostById(postId), HttpStatus.OK);
    }

    // Delete Post
    @DeleteMapping("/posts/{postId}")
    public ApiResponse deletePost(@PathVariable Integer postId){
        this.postService.deletePost(postId);
        return new ApiResponse("Post is Deleted Successfully! ", true);
    }

    // Update Post
    @PutMapping("/posts/{postId}")
    public ResponseEntity<PostDto> updatePost(@RequestBody PostDto postDto, @PathVariable Integer postId){
        return new ResponseEntity<>(this.postService.updatePost(postDto, postId), HttpStatus.OK);
    }

    // Search by keyword in title or content
    @GetMapping("/posts/search")
    public ResponseEntity<Page<PostDto>> searchInPost(
            @RequestParam("keyword") String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        Page<PostDto> posts = postService.searchPost(keyword, page, size);
        return ResponseEntity.ok(posts);
    }

    // Post Image Upload
    @PostMapping("/posts/image/upload/{postId}")
    public ResponseEntity<PostDto> uploadFile(@RequestParam MultipartFile image, @PathVariable Integer postId) throws IOException {
        PostDto postDto = this.postService.getPostById(postId);
        String fileName = this.fileService.uploadImage(path, image);
        postDto.setImageName(fileName);
        PostDto updatedPost = this.postService.updatePost(postDto, postId);

        return new ResponseEntity<>(updatedPost, HttpStatus.OK);
    }

    // Method to serve file
    @GetMapping(value = "/posts/images/{imageName}")
    public void downloadImage(@PathVariable("imageName") String imageName, HttpServletResponse response) throws IOException {
        InputStream resource = this.fileService.getResource(path, imageName);

        // Dynamically detect and set content type based on file extension
        String contentType = imageName.toLowerCase().endsWith(".png") ? MediaType.IMAGE_PNG_VALUE : MediaType.IMAGE_JPEG_VALUE;
        response.setContentType(contentType);

        StreamUtils.copy(resource, response.getOutputStream());
    }
}
