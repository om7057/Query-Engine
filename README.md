# MetaQuery - Query Engine

A Spring Boot REST API application that demonstrates secure, JWT-authenticated access to managed query workspaces. MetaQuery allows users to create workspaces, store database schemas, track query interactions, and manage SQL query generations with feedback mechanisms.

## 📋 Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Architecture](#architecture)
- [Tech Stack](#tech-stack)
- [Prerequisites](#prerequisites)
- [Installation & Setup](#installation--setup)
- [Configuration](#configuration)
- [Running the Application](#running-the-application)
- [API Endpoints](#api-endpoints)
- [Project Structure](#project-structure)
- [Key Components](#key-components)
- [Development](#development)

## 🎯 Overview

MetaQuery is a comprehensive query management platform built on Spring Boot 3.5.0. It provides a secure, multi-user environment for managing SQL workspaces, tracking query iterations, and storing database schema information. The application uses JWT-based authentication and PostgreSQL for data persistence.

**Current Version:** 0.0.1-SNAPSHOT  
**Java Version:** 17

## ✨ Features

- **User Authentication & Authorization**
  - Secure JWT-based authentication
  - User registration and login
  - HttpOnly cookie support for enhanced security
  
- **Workspace Management**
  - Create and manage isolated query workspaces
  - Store workspace descriptions and initial prompts
  - User-specific workspace access

- **Query Interactions**
  - Track user input and generated SQL queries
  - Support for multiple SQL dialects
  - Feedback mechanism for query quality tracking
  - Positive/negative feedback with comments

- **Schema Iterations**
  - Manage and track database schema versions
  - Store schema definitions and metadata
  - Version control for schema changes

- **Session Management**
  - User session tracking
  - Session-based security controls
  - Logout and session termination

- **Secure API**
  - Spring Security integration
  - JWT token validation
  - Role-based access control
  - CORS-ready configuration

## 🏗️ Architecture

The application follows a layered architecture pattern:

```
┌─────────────────────────────────┐
│   Controller Layer              │
│   (REST Endpoints)              │
├─────────────────────────────────┤
│   Service Layer                 │
│   (Business Logic)              │
├─────────────────────────────────┤
│   Repository Layer              │
│   (Data Access - JPA)           │
├─────────────────────────────────┤
│   Database Layer                │
│   (PostgreSQL)                  │
└─────────────────────────────────┘
```

**Security Architecture:**
- JWT tokens for stateless authentication
- Username/password authentication with Spring Security
- HttpOnly cookies for token storage
- Custom user details service for user loading

## 🛠️ Tech Stack

### Framework & Core
- **Spring Boot:** 3.5.0 (Web, Security, Data JPA)
- **Java:** 17
- **Build Tool:** Maven

### Database & Persistence
- **PostgreSQL:** Primary database (runtime dependency)
- **Spring Data JPA:** Object-relational mapping
- **Hibernate:** JPA implementation

### Security
- **Spring Security:** Authentication & authorization
- **JWT (JJWT):** Token generation and validation
  - jjwt-api: 0.11.5
  - jjwt-impl: 0.11.5
  - jjwt-jackson: 0.11.5

### Development Tools
- **Lombok:** Boilerplate reduction (@Data, @Builder, etc.)
- **SLF4J:** Logging

### Testing
- **JUnit & Spring Test:** Test framework
- **Maven Surefire:** Test runner

## 📋 Prerequisites

- **Java 17** or higher
- **Maven 3.6+** or use the provided Maven wrapper
- **PostgreSQL 12+**
- **Git** (for version control)

## 🚀 Installation & Setup

### 1. Clone the Repository

```bash
git clone <repository-url>
cd Query-Engine
```

### 2. Navigate to Server Directory

```bash
cd MetaQuery_Server
```

### 3. Set Up Database

Ensure PostgreSQL is running and create the database:

```bash
psql -U postgres
CREATE DATABASE metaquery;
\q
```

### 4. Configure Application

Copy the configuration template:

```bash
cp src/main/resources/application.properties.template src/main/resources/application.properties
```

Edit `application.properties` with your database credentials:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/metaquery
spring.datasource.username=postgres
spring.datasource.password=your_password
```

### 5. Build the Project

Using Maven wrapper (recommended):

```bash
./mvnw clean install
```

Or with system Maven:

```bash
mvn clean install
```

## ⚙️ Configuration

### Application Properties

Key configuration options in `application.properties`:

```properties
# Database Configuration
spring.datasource.url=jdbc:postgresql://localhost:5432/metaquery
spring.datasource.username=postgres
spring.datasource.password=your_password

# JPA/Hibernate Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

# Server Configuration
server.port=8080
server.servlet.context-path=/

# JWT Configuration (typically in application.properties or via environment variables)
jwt.secret=your_secret_key_here
jwt.expiration=86400000  # 24 hours in milliseconds

# Logging
logging.level.org.springframework=INFO
logging.level.org.spring.metaquery=DEBUG
```

### Environment Variables

Consider using environment variables for sensitive data:

```bash
export DATABASE_URL=jdbc:postgresql://localhost:5432/metaquery
export DATABASE_USERNAME=postgres
export DATABASE_PASSWORD=your_password
export JWT_SECRET=your_jwt_secret_key
```

## ▶️ Running the Application

### Development Mode

Using Maven wrapper:

```bash
./mvnw spring-boot:run
```

Or from the project root:

```bash
cd MetaQuery_Server
./mvnw spring-boot:run
```

### Production Build

```bash
./mvnw clean package
java -jar target/MetaQuery-0.0.1-SNAPSHOT.jar
```

The application will start at `http://localhost:8080`

### Database Migrations

Spring JPA will automatically handle schema creation based on entity mappings. For the first run, ensure `spring.jpa.hibernate.ddl-auto=update` or `create` is set.

## 📡 API Endpoints

### Authentication Endpoints

```
POST   /api/auth/signup          - Register a new user
POST   /api/auth/login           - Login and receive JWT token
POST   /api/auth/logout          - Logout (invalidate session)
```

### User Endpoints

```
GET    /api/users/{userId}       - Get user profile information
PUT    /api/users/{userId}       - Update user profile
GET    /api/users/{userId}/workspaces - List user's workspaces
```

### Workspace Endpoints

```
GET    /api/workspaces/{id}      - Get workspace details
POST   /api/workspaces           - Create a new workspace
PUT    /api/workspaces/{id}      - Update workspace information
DELETE /api/workspaces/{id}      - Delete a workspace
GET    /api/workspaces           - List all user workspaces
```

### Query Interaction Endpoints

```
POST   /api/queries              - Record a query interaction
GET    /api/queries/{id}         - Get query interaction details
GET    /api/queries/workspace/{workspaceId} - List queries in workspace
PUT    /api/queries/{id}/feedback - Submit feedback on a query
```

### Schema Iteration Endpoints

```
POST   /api/schemas              - Create a new schema iteration
GET    /api/schemas/{id}         - Get schema iteration details
GET    /api/schemas/workspace/{workspaceId} - List schemas in workspace
PUT    /api/schemas/{id}         - Update schema iteration
```

### Session Endpoints

```
GET    /api/sessions/current     - Get current session information
POST   /api/sessions/validate    - Validate JWT token
```

### Response Format

All endpoints return a standardized API response:

```json
{
  "success": true,
  "message": "Operation successful",
  "data": { /* response payload */ }
}
```

## 📁 Project Structure

```
Query-Engine/
├── pom.xml                          # Root Maven configuration
├── README.md                        # This file
├── scripts/                         # Utility scripts
│   ├── create_natural_commits.sh
│   └── create_timed_commits.sh
└── MetaQuery_Server/                # Main application module
    ├── mvnw                         # Maven wrapper (Unix)
    ├── mvnw.cmd                     # Maven wrapper (Windows)
    ├── pom.xml                      # Maven dependencies
    └── src/
        ├── main/
        │   ├── java/org/spring/metaquery/
        │   │   ├── MetaQueryApplication.java
        │   │   ├── config/
        │   │   │   └── WebSecurityConfig.java
        │   │   ├── controllers/
        │   │   │   ├── AuthController.java
        │   │   │   ├── QueryInteractionController.java
        │   │   │   ├── SchemaIterationController.java
        │   │   │   ├── SessionController.java
        │   │   │   ├── UserController.java
        │   │   │   └── WorkspaceController.java
        │   │   ├── dto/
        │   │   │   ├── ApiResponse.java
        │   │   │   ├── auth/
        │   │   │   │   ├── LoginRequest.java
        │   │   │   │   ├── LoginResponse.java
        │   │   │   │   ├── SignUpRequest.java
        │   │   │   │   └── SignUpResponse.java
        │   │   │   ├── query/
        │   │   │   ├── schema/
        │   │   │   ├── user/
        │   │   │   └── workspace/
        │   │   ├── entities/
        │   │   │   ├── QueryInteraction.java
        │   │   │   ├── SchemaIteration.java
        │   │   │   ├── User.java
        │   │   │   └── Workspace.java
        │   │   ├── enums/
        │   │   │   └── SqlDialect.java
        │   │   ├── exceptions/
        │   │   │   ├── GlobalExceptionHandler.java
        │   │   │   ├── user/
        │   │   │   └── workspace/
        │   │   ├── repositories/
        │   │   │   ├── QueryInteractionRepository.java
        │   │   │   ├── SchemaIterationRepository.java
        │   │   │   ├── UserRepository.java
        │   │   │   └── WorkspaceRepository.java
        │   │   ├── security/
        │   │   │   ├── CustomUserDetailsService.java
        │   │   │   ├── JwtAuthenticationFilter.java
        │   │   │   ├── JwtService.java
        │   │   │   └── UserPrincipal.java
        │   │   └── services/
        │   │       ├── QueryInteractionService.java
        │   │       ├── SchemaIterationService.java
        │   │       ├── UserService.java
        │   │       └── WorkspaceService.java
        │   └── resources/
        │       └── application.properties.template
        └── test/
            └── java/org/spring/metaquery/
                └── MetaQueryApplicationTests.java
```

## 🔑 Key Components

### 1. **Entities** (Data Models)

- **User:** Represents application users with authentication credentials
- **Workspace:** Isolated query environments belonging to users
- **QueryInteraction:** Records user queries and generated SQL with feedback
- **SchemaIteration:** Tracks database schema versions and metadata

### 2. **Controllers** (REST Endpoints)

- **AuthController:** Handles user registration, login, logout
- **UserController:** Manages user profiles and information
- **WorkspaceController:** CRUD operations for workspaces
- **QueryInteractionController:** Manages query interactions and feedback
- **SchemaIterationController:** Manages schema versions
- **SessionController:** Handles session validation and management

### 3. **Services** (Business Logic)

- **UserService:** User management, validation, and persistence
- **WorkspaceService:** Workspace operations and access control
- **QueryInteractionService:** Query tracking and feedback processing
- **SchemaIterationService:** Schema version management

### 4. **Security**

- **JwtService:** JWT token generation and validation
- **JwtAuthenticationFilter:** Intercepts requests and validates tokens
- **CustomUserDetailsService:** Loads user details for authentication
- **WebSecurityConfig:** Configures Spring Security filters and rules
- **UserPrincipal:** Represents authenticated user with roles/permissions

### 5. **Repositories** (Data Access)

Extends Spring Data JPA `CrudRepository` for database operations:

- **UserRepository:** User data access
- **WorkspaceRepository:** Workspace data access
- **QueryInteractionRepository:** Query interaction persistence
- **SchemaIterationRepository:** Schema version persistence

### 6. **DTOs** (Data Transfer Objects)

Request/response objects for API communication:

- **ApiResponse:** Standard response wrapper
- **LoginRequest/Response:** Authentication payloads
- **SignUpRequest/Response:** Registration payloads
- Workspace, Query, Schema, User specific DTOs

### 7. **Enums**

- **SqlDialect:** Supported SQL database dialects (PostgreSQL, MySQL, Oracle, etc.)

## 💻 Development

### Building the Project

```bash
./mvnw clean install
```

### Running Tests

```bash
./mvnw test
```

### Code Quality

The project uses:
- **Lombok** for reducing boilerplate
- **SLF4J** for structured logging
- **Spring conventions** for consistency

### Common Development Tasks

#### Adding a New Entity

1. Create entity class with JPA annotations in `entities/`
2. Create corresponding repository in `repositories/`
3. Create service class in `services/`
4. Create controller in `controllers/`
5. Create DTOs in `dto/`

#### Adding a New API Endpoint

1. Create method in appropriate controller
2. Implement business logic in service
3. Create/update DTOs for request/response
4. Add repository methods if needed
5. Document in API Endpoints section

#### Running with Different Database

Update `application.properties`:

```properties
# MySQL Example
spring.datasource.url=jdbc:mysql://localhost:3306/metaquery
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
```

### Logging

Configure logging levels in `application.properties`:

```properties
logging.level.org.spring.metaquery=DEBUG
logging.level.org.springframework.security=DEBUG
logging.level.org.hibernate.SQL=DEBUG
```

## 📝 Version History

- **0.0.1-SNAPSHOT** - Initial release with core functionality

## 📄 License

[Add your license information here]

## 👥 Contributing

[Add contribution guidelines here]

## 📞 Support & Contact

[Add contact information here]

---

**Last Updated:** March 29, 2026
