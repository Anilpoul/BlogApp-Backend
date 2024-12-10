package com.blogapp.repositories;

import com.blogapp.models.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepo extends JpaRepository<Comment, Integer> {
}
