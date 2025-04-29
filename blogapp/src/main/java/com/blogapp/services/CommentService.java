package com.blogapp.services;

import com.blogapp.payloads.CommentDto;

import java.security.Principal;
import java.util.List;

public interface CommentService {
    CommentDto createComment(CommentDto commentDto, Integer postId, Principal principal);
    List<CommentDto> getCommentsByPost(Integer postId);
    void deleteComment(Integer commentId);
}
