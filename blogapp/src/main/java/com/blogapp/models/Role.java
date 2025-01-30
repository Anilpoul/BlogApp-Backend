package com.blogapp.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;


public enum Role {
    ROLE_USER,
    ROLE_ADMIN;
}
