Certainly! Here's a comprehensive guide to building an **Admin Dashboard** that displays post statistics using **Spring Boot (Java) with PostgreSQL** for the backend and **React with Recharts** for the frontend. This setup includes role-based access control to ensure only admins can view the statistics.

---

## 📁 Project Structure

```
blog-app/
├── backend/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── com/
│   │   │   │       └── blog/
│   │   │   │           ├── BlogApplication.java
│   │   │   │           ├── controller/
│   │   │   │           │   └── AdminController.java
│   │   │   │           ├── model/
│   │   │   │           │   ├── Post.java
│   │   │   │           │   └── User.java
│   │   │   │           ├── repository/
│   │   │   │           │   └── PostRepository.java
│   │   │   │           └── security/
│   │   │   │               └── SecurityConfig.java
├── frontend/
│   ├── public/
│   └── src/
│       ├── components/
│       │   └── PostStats.js
│       ├── App.js
│       └── index.js
└── README.md
```

---

## 🛠 Backend: Spring Boot with PostgreSQL

### 1. **Set Up Spring Boot Project**

Use [Spring Initializr](https://start.spring.io/) to generate a Spring Boot project with the following dependencies:

- Spring Web
- Spring Data JPA
- PostgreSQL Driver
- Spring Security
- Spring Boot DevTools

### 2. **Configure `application.properties`**

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/blogdb
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
```

### 3. **Define Entities**

#### `User.java`

```java
package com.blog.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class User {
    @Id
    private Long id;
    private String username;
    private String role;

    // Getters and Setters
}
```

#### `Post.java`

```java
package com.blog.model;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
public class Post {
    @Id
    private Long id;
    private String title;
    private LocalDateTime createdAt;

    @ManyToOne
    private User author;

    // Getters and Setters
}
```

### 4. **Create DTO for Post Statistics**

```java
package com.blog.dto;

public class PostCountDTO {
    private String username;
    private Long postCount;

    // Constructor, Getters and Setters
}
```

### 5. **Repository Layer**

#### `PostRepository.java`

```java
package com.blog.repository;

import com.blog.dto.PostCountDTO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PostRepository extends CrudRepository<Post, Long> {
    @Query("SELECT new com.blog.dto.PostCountDTO(p.author.username, COUNT(p)) " +
           "FROM Post p GROUP BY p.author.username")
    List<PostCountDTO> countPostsByUser();
}
```

### 6. **Controller Layer**

#### `AdminController.java`

```java
package com.blog.controller;

import com.blog.dto.PostCountDTO;
import com.blog.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private PostRepository postRepository;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/post-stats")
    public List<PostCountDTO> getPostStats() {
        return postRepository.countPostsByUser();
    }
}
```

### 7. **Security Configuration**

#### `SecurityConfig.java`

```java
package com.blog.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .antMatchers("/api/admin/**").hasRole("ADMIN")
                .anyRequest().permitAll()
            .and()
            .httpBasic();
    }
}
```

---

## ⚛️ Frontend: React with Recharts

### 1. **Set Up React Project**

```bash
npx create-react-app frontend
cd frontend
npm install recharts axios
```

### 2. **Create `PostStats.js` Component**

```jsx
import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { PieChart, Pie, Cell, Tooltip, Legend } from 'recharts';

const COLORS = ['#0088FE', '#00C49F', '#FFBB28', '#FF8042'];

const PostStats = () => {
  const [data, setData] = useState([]);

  useEffect(() => {
    axios.get('http://localhost:8080/api/admin/post-stats', {
      headers: {
        Authorization: `Bearer ${sessionStorage.getItem('authToken')}`
      }
    })
    .then(res => setData(res.data))
    .catch(err => console.error(err));
  }, []);

  return (
    <div>
      <h2>Posts by User</h2>
      <PieChart width={400} height={300}>
        <Pie
          data={data}
          dataKey="postCount"
          nameKey="username"
          cx="50%"
          cy="50%"
          outerRadius={100}
          label
        >
          {data.map((entry, index) => (
            <Cell key={entry.username} fill={COLORS[index % COLORS.length]} />
          ))}
        </Pie>
        <Tooltip />
        <Legend />
      </PieChart>
    </div>
  );
};

export default PostStats;
```

### 3. **Modify `App.js`**

```jsx
import React from 'react';
import PostStats from './components/PostStats';

function App() {
  return (
    <div className="App">
      <PostStats />
    </div>
  );
}

export default App;
```

### 4. **Run the Application**

- Start the backend:

```bash
cd backend
./mvnw spring-boot 
