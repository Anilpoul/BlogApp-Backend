# 🚀 Full-Stack Project Deployment Guide (React + Spring Boot + Supabase)

## 📚 Overview
This project contains a **React frontend**, **Spring Boot backend**, and a **Supabase PostgreSQL database**, deployed for free using **Vercel**, **Render**, and **Supabase**.

---

## 📦 Project Structure
```
- frontend/  (React app)
- backend/   (Spring Boot app)
- database/  (Supabase PostgreSQL - Cloud)
```

---

## 🛠️ Deployment Steps

### 1. 🛢️ Deploy PostgreSQL Database on Supabase
- Sign up at [supabase.com](https://supabase.com/).
- Create a **new project**.
- Save the following details:
  - Database URL
  - Database username
  - Database password

---

### 2. ⚙️ Deploy Backend (Spring Boot API) on Render
- Push your backend code to **GitHub**.
- Go to [render.com](https://render.com/) and click **New Web Service**.
- Connect your GitHub repository.
- Settings:
  - **Environment**: Java (or Docker if using container)
  - **Build Command**:
    ```
    ./gradlew build   # (for Gradle)
    ```
    or
    ```
    mvn clean install # (for Maven)
    ```
  - **Start Command**:
    ```
    java -jar target/yourapp.jar
    ```
- Add **Environment Variables**:
  - `DB_URL`
  - `DB_USERNAME`
  - `DB_PASSWORD`
  - `ALLOWED_ORIGIN`

---

### 3. 🌐 Deploy Frontend (React App) on Vercel
- Push your frontend code to **GitHub**.
- Go to [vercel.com](https://vercel.com/) and click **New Project**.
- Connect your GitHub repository.
- Add **Environment Variable**:
  - `REACT_APP_API_URL=https://your-backend-service.onrender.com`
- Deploy your frontend and save the URL.

---

## 📄 Environment Variables

### For React (`frontend/.env`)
```
REACT_APP_API_URL=https://your-backend-service.onrender.com
```

### For Spring Boot (`backend/src/main/resources/application.properties`)
```
spring.datasource.url=${DB_URL}
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
allowed.origin=${ALLOWED_ORIGIN}

# Hibernate Settings
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

---

## 🔗 Final Setup
- Frontend ➔ Calls backend API hosted on Render.
- Backend ➔ Connects to PostgreSQL database hosted on Supabase.

---

## ✅ Deployment Checklist

| Step | Status |
|-----|------|
| Create Supabase Database | 🔲 |
| Deploy Backend to Render | 🔲 |
| Deploy Frontend to Vercel | 🔲 |
| Configure Environment Variables | 🔲 |
| Test Frontend ↔️ Backend Connection | 🔲 |
| Test Backend ↔️ Database Connection | 🔲 |

---

## 🎯 Quick Example of Frontend Fetch Call

In your React app:

```javascript
const apiUrl = process.env.REACT_APP_API_URL;

async function fetchData() {
  try {
    const response = await fetch(`${apiUrl}/api/data`);
    const data = await response.json();
    console.log(data);
  } catch (error) {
    console.error('Error fetching data:', error);
  }
}
```

✅ This will correctly call your backend API deployed on Render!

---

## 📌 Important Notes
- Always restart your frontend after editing `.env`.
- Use HTTPS URLs in production for secure communication.
- Monitor usage limits on Supabase, Render, and Vercel free plans.

---

# 🎉 Congratulations! Your Full-Stack Project is Live! 🎉
