# Campus Lost and Found - Project Report

## Project Title

Campus Lost and Found

## Team Members

- Kunwar Bibek
- Danuwar Rajim
- Pokhrel Bidhan

## Project Overview

Campus Lost and Found is a web application that helps students post and search for lost or found items on campus. Users can create an account, upload item photos, search posts by keyword, and manage their own item posts.

## Main Features

- User authentication: sign up, login, logout
- Lost/found item CRUD: create, read, update, delete
- Image upload and display
- Keyword search
- PostgreSQL database with JPA
- Responsive user interface
- Render deployment support

## Technologies Used

- Java 17
- Spring Boot
- Spring MVC
- Spring Security
- Spring Data JPA
- PostgreSQL
- Thymeleaf
- HTML and CSS
- Maven
- Render
- GitHub

## Database Design

### app_users table

| Field | Type | Description |
| --- | --- | --- |
| id | Long | Primary key |
| name | String | User name |
| email | String | Unique login email |
| password | String | Encrypted password |
| role | String | User role |

### lost_item table

| Field | Type | Description |
| --- | --- | --- |
| id | Long | Primary key |
| title | String | Item title |
| category | String | Item category |
| location | String | Campus location |
| description | String | Item details |
| status | Enum | LOST, FOUND, or RETURNED |
| image_path | String | Uploaded image URL |
| created_at | DateTime | Created time |
| updated_at | DateTime | Updated time |
| owner_id | Long | User who created the post |

## Screenshots

Add screenshots after running the deployed website:

- Home and item list page
- Sign up page
- Login page
- Create item page
- Item detail page
- Search results page

## Development Process

1. Selected a practical project topic for students.
2. Designed the database entities for users and item posts.
3. Built authentication using Spring Security.
4. Implemented item CRUD with Spring MVC and JPA repositories.
5. Added image upload and static file display.
6. Added keyword search.
7. Created responsive Thymeleaf pages.
8. Prepared GitHub and Render deployment files.

## Problems and Solutions

| Problem | Solution |
| --- | --- |
| Users should only edit their own posts | Checked the logged-in user's email before edit, update, or delete |
| Uploaded images need to stay available on Render free tier | Stored image data in PostgreSQL instead of using a paid persistent disk |
| The app needs PostgreSQL on Render but easy local testing | Used H2 by default and PostgreSQL through environment variables |
| Search should cover multiple fields | Used a JPA repository method to search title, category, location, and description |

## Conclusion

Campus Lost and Found satisfies the final project requirements by combining authentication, PostgreSQL/JPA database access, CRUD functions, image upload, search, web design, GitHub readiness, and Render deployment support. The project is simple enough for a three-student team to explain clearly, while still demonstrating the main technologies learned in class.
