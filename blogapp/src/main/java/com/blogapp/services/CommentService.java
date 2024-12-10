package com.blogapp.services;

import com.blogapp.payloads.CommentDto;

public interface CommentService {
    CommentDto createComment(CommentDto commentDto, Integer userID, Integer postId);
    CommentDto getComment(Integer commentId);
    void deleteComment(Integer commentId);
}
