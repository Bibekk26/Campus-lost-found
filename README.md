# Campus Lost and Found

A Spring Boot final project web application where students can post lost or found campus items, upload photos, search by keyword, and manage their own posts.

## Features

- User sign up, login, and logout with Spring Security
- PostgreSQL database support
- JPA entities and repositories
- CRUD for lost/found item posts
- Image upload and display, stored in the database for Render free tier
- Keyword search across title, category, location, and description
- Responsive Thymeleaf UI
- Render-ready deployment files

## Tech Stack

- Java 17
- Spring Boot 3
- Spring MVC
- Spring Security
- Spring Data JPA
- PostgreSQL
- Thymeleaf
- Maven

## Local Development

The app uses H2 by default so it can run immediately for testing.

```bash
mvn spring-boot:run
```

Open `http://localhost:8080`.

## PostgreSQL Configuration

Set these environment variables when using PostgreSQL:

```bash
DATABASE_URL=jdbc:postgresql://localhost:5432/lostfound
DATABASE_USERNAME=postgres
DATABASE_PASSWORD=your_password
DATABASE_DRIVER=org.postgresql.Driver
SPRING_PROFILES_ACTIVE=postgres
```

## Render Deployment

1. Push this project to GitHub.
2. Create a PostgreSQL database in Render.
3. Create a Web Service connected to the GitHub repository.
4. Use:
   - Build command: `mvn clean package -DskipTests`
   - Start command: `java -jar target/lost-found-0.0.1-SNAPSHOT.jar`
5. Add environment variables:
   - `SPRING_PROFILES_ACTIVE=postgres`
   - `DATABASE_URL`
   - `DATABASE_USERNAME`
   - `DATABASE_PASSWORD`
   - `DATABASE_DRIVER=org.postgresql.Driver`

## Test Account

Create an account from the Sign Up page, then use it to create, edit, and delete item posts.
