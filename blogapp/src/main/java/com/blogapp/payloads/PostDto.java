package com.blogapp.payloads;

import com.blogapp.models.Category;
import com.blogapp.models.Comment;
import com.blogapp.models.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class PostDto {

    private Integer postId;

    private String postTitle;
    private String content;
    private String imageName;
    private Date creationDate;
    private UserResponseDto user;
    private CategoryDto category;

    private Set<CommentDto> comments = new HashSet<>();

}
