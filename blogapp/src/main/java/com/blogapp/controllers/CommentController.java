package com.blogapp.controllers;

import com.blogapp.payloads.ApiResponse;
import com.blogapp.payloads.CommentDto;
import com.blogapp.services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://127.0.0.1:5500")
public class CommentController {
    @Autowired
    private CommentService commentService;

    @PostMapping("/user/{userId}/posts/{postId}/comments")
    public ResponseEntity<CommentDto> createComment(@RequestBody CommentDto commentDto,
                                                    @PathVariable Integer postId,
                                                    @PathVariable Integer userId){
        CommentDto createdComment = this.commentService.createComment(commentDto, userId, postId);

        return new ResponseEntity<CommentDto>(createdComment, HttpStatus.CREATED);
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<ApiResponse> deleteComment(@PathVariable Integer commentId){
        this.commentService.deleteComment(commentId);
        return new ResponseEntity<ApiResponse>(new ApiResponse("Comment deleted Successfully", true), HttpStatus.OK);
    }
}
