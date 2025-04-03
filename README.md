# Blog App Structure

## src/index.js
```javascript
import React from 'react';
import ReactDOM from 'react-dom/client';
import App from './App';
import { BrowserRouter } from 'react-router-dom';
import 'bootstrap/dist/css/bootstrap.min.css';

const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
  <BrowserRouter>
    <App />
  </BrowserRouter>
);
```

## src/services/api.js
```javascript
import axios from 'axios';

const API_URL = 'http://localhost:5000/api';

export const getBlogs = async () => {
  try {
    const response = await axios.get(`${API_URL}/blogs`);
    return response.data;
  } catch (error) {
    console.error('Error fetching blogs:', error);
    return [];
  }
};

export const getBlogById = async (id) => {
  try {
    const response = await axios.get(`${API_URL}/blogs/${id}`);
    return response.data;
  } catch (error) {
    console.error('Error fetching blog:', error);
    return null;
  }
};

export const createBlog = async (blogData) => {
  try {
    const response = await axios.post(`${API_URL}/blogs`, blogData);
    return response.data;
  } catch (error) {
    console.error('Error creating blog:', error);
    return null;
  }
};

export const updateBlog = async (id, blogData) => {
  try {
    const response = await axios.put(`${API_URL}/blogs/${id}`, blogData);
    return response.data;
  } catch (error) {
    console.error('Error updating blog:', error);
    return null;
  }
};
```

## src/components/Navbar.js
```javascript
import React from 'react';
import { Navbar, Nav, Container } from 'react-bootstrap';
import { Link } from 'react-router-dom';

const Navigation = () => {
  return (
    <Navbar bg='dark' variant='dark' expand='lg'>
      <Container>
        <Navbar.Brand as={Link} to='/'>BlogApp</Navbar.Brand>
        <Nav className='ms-auto'>
          <Nav.Link as={Link} to='/create'>Create Blog</Nav.Link>
          <Nav.Link as={Link} to='/login'>Login</Nav.Link>
          <Nav.Link as={Link} to='/register'>Register</Nav.Link>
        </Nav>
      </Container>
    </Navbar>
  );
};

export default Navigation;
```

## src/components/BlogList.js
```javascript
import React, { useEffect, useState } from 'react';
import { getBlogs } from '../services/api';
import { Link } from 'react-router-dom';
import { ListGroup, Container } from 'react-bootstrap';

const BlogList = () => {
  const [blogs, setBlogs] = useState([]);

  useEffect(() => {
    const fetchBlogs = async () => {
      const data = await getBlogs();
      setBlogs(data);
    };
    fetchBlogs();
  }, []);

  return (
    <Container>
      <h2>All Blogs</h2>
      <ListGroup>
        {blogs.map((blog) => (
          <ListGroup.Item key={blog.id}>
            <Link to={`/blogs/${blog.id}`}>{blog.title}</Link>
          </ListGroup.Item>
        ))}
      </ListGroup>
    </Container>
  );
};

export default BlogList;
```

## src/pages/Home.js
```javascript
import React from 'react';
import BlogList from '../components/BlogList';

const Home = () => {
  return (
    <div>
      <h1>Welcome to Blog App</h1>
      <BlogList />
    </div>
  );
};

export default Home;
```

## src/pages/LoginPage.js
```javascript
import React from 'react';
import Login from '../components/Login';

const LoginPage = () => {
  return (
    <div>
      <h2>Login</h2>
      <Login />
    </div>
  );
};

export default LoginPage;
```

## src/pages/RegisterPage.js
```javascript
import React from 'react';
import Register from '../components/Register';

const RegisterPage = () => {
  return (
    <div>
      <h2>Register</h2>
      <Register />
    </div>
  );
};

export default RegisterPage;
```

## src/App.js
```javascript
import React from 'react';
import { Routes, Route } from 'react-router-dom';
import Navigation from './components/Navbar';
import Home from './pages/Home';
import BlogDetails from './pages/BlogDetails';
import CreateBlog from './pages/CreateBlog';
import LoginPage from './pages/LoginPage';
import RegisterPage from './pages/RegisterPage';
import { Container } from 'react-bootstrap';

const App = () => {
  return (
    <>
      <Navigation />
      <Container className='mt-4'>
        <Routes>
          <Route path='/' element={<Home />} />
          <Route path='/blogs/:id' element={<BlogDetails />} />
          <Route path='/create' element={<CreateBlog />} />
          <Route path='/login' element={<LoginPage />} />
          <Route path='/register' element={<RegisterPage />} />
        </Routes>
      </Container>
    </>
  );
};

export default App;
```

This updated structure now includes all components and pages. Let me know if anything else needs improvement! 🚀
