# Student Management System (Spring Boot + Hibernate + MySQL REST API)

A clean, layered **Spring Boot 3 + Spring Data JPA (Hibernate) + MySQL** REST API demo project designed for learning Java backend development and opening directly in **IntelliJ IDEA**.

---

## 1. Architecture & Project Structure

This project follows the standard enterprise 4-layer Spring Boot architecture:

```text
Controller Layer  (@RestController)   -> Handles HTTP requests/responses & validation
       ↓
Service Layer     (@Service)          -> Contains business logic & transaction boundaries (@Transactional)
       ↓
Repository Layer  (@Repository)       -> Interfaces with the database via Spring Data JPA / Hibernate
       ↓
Database Layer    (MySQL)             -> Stores relational data mapped to @Entity classes
```

### Directory Layout
```text
student_management_google/
├── pom.xml                                      # Maven dependencies (Spring Web, Data JPA, Validation, MySQL)
├── sql/
│   └── schema.sql                               # SQL script to create database & table manually (optional)
└── src/
    ├── main/
    │   ├── java/com/example/studentmanagement/
    │   │   ├── StudentManagementApplication.java  # Main entry point (@SpringBootApplication)
    │   │   ├── controller/
    │   │   │   └── StudentController.java         # REST API endpoints (/api/v1/students)
    │   │   ├── service/
    │   │   │   ├── StudentService.java            # Service interface
    │   │   │   └── impl/StudentServiceImpl.java   # Business logic & DTO <-> Entity mapping
    │   │   ├── repository/
    │   │   │   └── StudentRepository.java         # Spring Data JPA repository
    │   │   ├── entity/
    │   │   │   └── Student.java                   # Hibernate @Entity mapped to 'students' table
    │   │   ├── dto/
    │   │   │   ├── StudentRequestDto.java         # Incoming request payload with @Valid rules
    │   │   │   └── StudentResponseDto.java        # Outgoing JSON response payload
    │   │   └── exception/
    │   │       ├── GlobalExceptionHandler.java    # @RestControllerAdvice centralized error handling
    │   │       ├── ResourceNotFoundException.java # 404 Not Found exception
    │   │       ├── DuplicateResourceException.java# 409 Conflict exception
    │   │       └── ErrorResponse.java             # Standardized JSON error structure
    │   └── resources/
    │       └── application.yml                    # MySQL & Hibernate configuration
    └── test/
        ├── java/com/example/studentmanagement/
        │   └── controller/StudentControllerIntegrationTest.java # Automated REST API tests
        └── resources/
            └── application.yml                    # In-memory H2 config for running tests offline
```

---

## 2. How to Open & Run in IntelliJ IDEA

1. **Prepare MySQL**:
   Make sure your local MySQL server is running on port `3306`, or run `sql/schema.sql`:
   ```sql
   CREATE DATABASE IF NOT EXISTS student_db_google;
   ```
   *(Note: `createDatabaseIfNotExist=true` and `ddl-auto: update` are enabled in `src/main/resources/application.yml`, so Hibernate will automatically create the database and `students` table on startup!)*

2. **Configure MySQL Credentials**:
   Check `src/main/resources/application.yml` and update `username` and `password` (defaults to `root` / `root`, or pass environment variables `DB_USERNAME` and `DB_PASSWORD`).

3. **Open in IntelliJ IDEA**:
   - Launch **IntelliJ IDEA** -> **File** -> **Open...**
   - Select this folder (`/home/mikeli94/code/student_management_google`) and click **OK**.
   - IntelliJ will detect `pom.xml` and automatically import all Maven dependencies.
   - Open `src/main/java/com/example/studentmanagement/StudentManagementApplication.java` and click the green **Run (▶)** icon next to `main()`.

---

## 3. REST API Endpoints & Sample `curl` Requests

Base URL: `http://localhost:8083/api/v1/students`

### 1) Create a Student (`POST /api/v1/students`)
```bash
curl -X POST http://localhost:8083/api/v1/students \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "Alice",
    "lastName": "Smith",
    "email": "alice.smith@university.edu",
    "dateOfBirth": "2003-05-14",
    "major": "Computer Science",
    "gpa": 3.85
  }'
```

### 2) Get All Students (`GET /api/v1/students`)
```bash
# Get all students
curl http://localhost:8083/api/v1/students

# Filter by major
curl "http://localhost:8083/api/v1/students?major=Computer%20Science"
```

### 3) Get Student by ID (`GET /api/v1/students/{id}`)
```bash
curl http://localhost:8083/api/v1/students/1
```

### 4) Update a Student (`PUT /api/v1/students/{id}`)
```bash
curl -X PUT http://localhost:8083/api/v1/students/1 \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "Alice",
    "lastName": "Johnson",
    "email": "alice.smith@university.edu",
    "dateOfBirth": "2003-05-14",
    "major": "Data Science",
    "gpa": 3.95
  }'
```

### 5) Delete a Student (`DELETE /api/v1/students/{id}`)
```bash
curl -X DELETE http://localhost:8083/api/v1/students/1
```
