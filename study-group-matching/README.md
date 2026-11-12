# Study Group Matching

Study Group Matching is an ACM Project Committee Fall 2026 project designed to help NC State students find and form study groups based on their courses, availability, and study preferences.

The project focuses on building a simple and reliable MVP first, with additional features added as time allows.

---

## Project Goals

- Help NC State students discover classmates who want to study together.
- Allow students to create and join course-based study groups.
- Use study preferences and availability to improve group matching.
- Provide built-in communication between study group members.
- Build a maintainable MVP before implementing optional features.

--- 

## MVP Features

The planned MVP includes:

- User profiles
- Course and section selection
- Study preferences
- Study group discovery
- Study group creation
- Join requests
- Accepting or rejecting join requests
- Study group membership
- User dashboard
- Built-in study group chat
- Basic rules-based study group matching

---

## Tech Stack

### Backend
- Java 21
- Spring Boot 4.1.1
- Spring Web MVC
- Spring Data JPA

### Frontend
- HTML
- CSS
- JavaScript

### Database
- MySQL

### Testing
- JUnit
- Spring Boot Test
- MockMvc
- Mockito
- H2 in-memory database for automated tests

### Build Tool
- Maven

### Version Control
- Git
- GitHub

---

## Project Structure

```text
src/
├── main/
│   ├── java/
│   │   └── edu/ncsu/acm/studygroup/
│   │       ├── config/
│   │       ├── controller/
│   │       ├── dto/
│   │       ├── entity/
│   │       ├── repository/
│   │       ├── service/
│   │       └── StudyGroupMatchingApplication.java
│   │
│   └── resources/
│       ├── static/
│       │   ├── css/
│       │   ├── js/
│       │   └── index.html
│       └── application.properties
│
└── test/
    ├── java/
    └── resources/
        └── application.properties
```

## Package Overview
- entity - Core application objects and database entities.
- repository - Handles database access using Spring Data JPA.
- service - Contains application and business logic.
- controller - Provides REST API endpoints.
- dto - Transfers request data between the frontend and backend.
- config - Contains application-wide configuration.

---

## Main Domain Classes
The current domain model contains:
- User
- Course
- Section
- Enrollment
- StudyPreference
- StudyGroup
- GroupMember
- JoinRequest
- Message
  These classes are based on the team's UML design.

---

## Local Setup
Requirements
Before running the application, install:
- Java 21
- Maven
- MySQL
- Git
- IntelliJ IDEA or another Java IDE

---

## Database Setup
Create a local MySQL database:
CREATE DATABASE study_group_matching;

The application uses environment variables for database credentials.
Required environment variables:
DB_USERNAME
DB_PASSWORD

Optional:
DB_URL

If DB_URL is not provided, the application uses:
jdbc:mysql://localhost:3306/study_group_matching

Do not commit database passwords or other credentials to GitHub.

---

## Running the Application
Start the Spring Boot application from:
StudyGroupMatchingApplication.java

The application runs at:
http://localhost:8080

---

## Health Check
To verify that the backend is running, open:
http://localhost:8080/api/health

Expected response:
{
"status": "UP",
"message": "Study Group Matching API is running"
}

--- 

## Running Tests
Run:
mvn test

Automated tests use an H2 in-memory database, so a local MySQL connection is not required for running the test suite.

## Git Workflow
The project uses the following branch structure:
main
↑
development
↑
feature branches

main
Contains stable project versions.
Do not directly develop on main.
development
Main integration branch for ongoing development.
Completed features should be merged into development through pull requests.
Feature Branches
Each task should be developed in its own feature branch.
Examples:
feature/user-profile
feature/study-group-api
feature/dashboard-ui
feature/group-chat

Typical workflow:
git checkout development
git pull

git checkout -b feature/example-feature

# Make changes

git add .
git commit -m "Implement example feature"
git push -u origin feature/example-feature

Then create a pull request from the feature branch into development.

---

## Testing Responsibility
All team members are responsible for testing the functionality they implement.
Tests should be added when new backend functionality is introduced.

---

## Documentation
Each team member should document the portion of the project they work on.
The Project Manager will maintain the overall project documentation and coordinate integration between project components.

---
