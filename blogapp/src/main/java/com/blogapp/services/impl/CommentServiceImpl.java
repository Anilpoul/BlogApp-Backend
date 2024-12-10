package com.blogapp.services.impl;

import com.blogapp.exceptions.ResourceNotFoundException;
import com.blogapp.models.Comment;
import com.blogapp.models.Post;
import com.blogapp.models.User;
import com.blogapp.payloads.CommentDto;
import com.blogapp.repositories.CommentRepo;
import com.blogapp.repositories.PostRepo;
import com.blogapp.repositories.UserRepo;
import com.blogapp.services.CommentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private PostRepo postRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private CommentRepo commentRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CommentDto createComment(CommentDto commentDto, Integer userId,Integer postId) {
        Post post = this.postRepo.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post ", " postId ", postId));

        User
                user = this.userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User ", " userID ", userId));
        Comment comment = this.modelMapper.map(commentDto, Comment.class);
        comment.setUser(user);
        comment.setPost(post);
        Comment savedComment = this.commentRepo.save(comment);
        return this.modelMapper.map(savedComment, CommentDto.class);
    }

    @Override
    public CommentDto getComment(Integer commentId) {
        return null;
    }

    @Override
    public void deleteComment(Integer commentId) {

        Comment comment = this.commentRepo.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment ", " CommentId ", commentId));
        this.commentRepo.delete(comment);
    }
}
