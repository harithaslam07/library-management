# Library Management System

A REST API for managing a library: books, members, issuing and returning books, with automatic fine calculation.

Built with Spring Boot and MySQL.

## Tech Stack

- Java 25
- Spring Boot 4.1.1
- Spring Data JPA (Hibernate) and MySQL
- Bean Validation
- Lombok
- Swagger UI (springdoc-openapi)
- JUnit 5 and Mockito

## Features

- Book management with search by category, author and title
- Pagination and sorting for book lists
- Member registration with duplicate email protection
- Issue a book: checks stock, blocks double loans, sets a 14-day due date
- Return a book: restores stock and calculates a fine of 5 per late day
- Transactional operations, so stock and loan records stay consistent
- Request and response DTOs, so entities are never exposed
- Global exception handling with clean JSON errors (400, 404, 409)
- Unit tests for the issue and return rules

## Project Structure

```
src/main/java/com/project/library_management
├── controller    REST endpoints
├── service       business rules
├── repository    database access (Spring Data JPA)
├── model         JPA entities: Book, Member, Issue
├── dto           request and response classes
└── exception     custom exceptions and global handler
```

## API Overview

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/books` | Add a book |
| GET | `/api/books` | List books. Filters: `category`, `author`, `title`. Paging: `page`, `size`, `sort` |
| GET | `/api/books/{id}` | Get one book |
| PUT | `/api/books/{id}` | Update a book |
| DELETE | `/api/books/{id}` | Delete a book |
| POST | `/api/members` | Register a member |
| GET | `/api/members` | List members |
| GET | `/api/members/{id}` | Get one member |
| POST | `/api/issues` | Issue a book to a member |
| POST | `/api/issues/{id}/return` | Return a book and calculate the fine |

Full interactive documentation is available in Swagger UI once the app is running.

## Business Rules

- A book can be issued only if copies are available.
- A member cannot hold two copies of the same book at once.
- The loan period is 14 days.
- A late return costs 5 per day after the due date.
- Returning an already returned book is rejected.

## Error Format

```json
{
  "status": 404,
  "error": "Not Found",
  "message": "Book not found: 99",
  "timestamp": "2026-10-08T10:15:30"
}
```

## Getting Started

### Prerequisites

- JDK 25
- MySQL 8 or later

### Setup

1. Clone the repository:
```bash
   git clone https://github.com/harithaslam07/library-management.git
   cd library-management
```
2. Create the database:
```sql
   CREATE DATABASE library_db;
```
3. Set the database password as an environment variable:
```
   DB_PASSWORD=your_mysql_password
```
The username defaults to `root`. Change it in `src/main/resources/application.properties` if needed.
4. Run the application:
```bash
   mvnw spring-boot:run
```
5. Open Swagger UI:
```
   http://localhost:8080/swagger-ui/index.html
```

## Running Tests

```bash
mvnw -Dtest=IssueServiceTest test
```

## Future Improvements

- JWT authentication with roles
- Database migrations with Flyway
- Combined search filters
- Scheduled job to flag overdue books
- Deployment