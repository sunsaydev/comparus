# User Aggregation Service

This project is a **User Aggregation Service** built with **Spring Boot**. The service aggregates user data from multiple databases and provides a single REST endpoint for selecting data.

## Features

- Aggregate user data from multiple databases.
- Provides a single `GET /users` endpoint to retrieve user data from all configured data sources.
- Configurable database connections through declarative configuration.

## Requirements

- **Java 17** or higher
- **Maven**
- **PostgreSQL** database

## Setup

### 1. Clone the Repository

```bash
git clone https://github.com/sunsaydev/comparus.git
```

### 2. In file application.yml specify databases, table names and fields

### 3. Make sure your databases containing data in specified tables

### 4. Run application

### 5. Use Postman or other tool and use this endpoint http://localhost:8080/users