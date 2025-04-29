package com.blogapp.repositories;

import com.blogapp.models.Category;
import com.blogapp.models.Post;
import com.blogapp.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepo extends JpaRepository<Post, Integer> {
    List<Post> findByUser(User user);
    Page<Post> findByCategory(Category category, Pageable pageable);
    Page<Post> findByPostTitleContainingIgnoreCaseOrContentContainingIgnoreCase(String title, String content, Pageable pageable);
}
