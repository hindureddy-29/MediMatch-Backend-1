# MediMatch AI – Spring Boot Backend

MediMatch AI backend is an enterprise-grade RESTful web service built with **Spring Boot 3**, **Spring Data JPA**, **Hibernate**, and **MySQL**, featuring NLP and algorithmic duplicate medicine detection.

## Key Features

- **Duplicate Medicine Detection**:
  - Medicine name normalization (dosage and form parsing, symbol removal, phonetic cleaning).
  - String similarity algorithms (Levenshtein distance, Jaro-Winkler similarity, and hybrid score).
  - Active ingredient matching and brand-to-generic mapping.
  - Multi-classification categorization:
    - `Exact name match`
    - `Possible ingredient duplicate`
    - `Possible name match`
    - `Uncertain match`
    - `Insufficient medicine information`
  - Clinical safety notices (dosage strength mismatch alerts, form variation notices, LASA sound-alike warnings).
- **Medicine Database Management**:
  - Full CRUD operations (`GET`, `POST`, `PUT`, `DELETE`).
  - Search by brand name, generic name, active ingredient, or category.
  - Pre-seeded with 50+ verified pharmaceutical medicines.
- **Analysis Sessions & History**:
  - Preserves medication batch analysis sessions.
  - Historical lookup and session deletion.
- **Real-Time Analytics Dashboard**:
  - Metrics on analyzed medications, duplicate frequencies, classification distributions.
- **Security & Authentication**:
  - JWT (JSON Web Token) authentication with stateless session management.
  - BCrypt password encryption.

## Prerequisites

- Java 17+ (Java 25 LTS supported)
- Maven 3.8+
- MySQL Server 8.0+

## Local Configuration

Create a `.env` file or export environment variables:

```bash
DB_URL=jdbc:mysql://localhost:3306/medimatch_db?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
DB_USERNAME=root
DB_PASSWORD=your_mysql_password
JWT_SECRET=MediMatchSecretKeyForJwtTokenGeneration2026SecureKeyWithAtLeast256Bits
SPRING_PROFILES_ACTIVE=mysql
```

Alternatively, run with H2 in-memory profile for zero-configuration testing:
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=h2
```

## Running the Application

### Using Maven:
```bash
mvn clean install
mvn spring-boot:run
```

### Running in VS Code:
1. Open the `backend` folder in VS Code.
2. Install the **Extension Pack for Java** and **Spring Boot Extension Pack**.
3. Press `F5` or click "Run" above `com.medimatch.MediMatchApplication.main()`.

## Running with Docker:
```bash
docker build -t medimatch-backend .
docker run -p 8080:8080 -e DB_URL="jdbc:mysql://host.docker.internal:3306/medimatch_db" -e DB_USERNAME="root" -e DB_PASSWORD="password" medimatch-backend
```

## REST API Reference

| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `POST` | `/api/detection/analyze` | Analyze single/multiple medicines for duplicate therapies |
| `GET` | `/api/medicines` | List / search medicine catalog (`?search=query`) |
| `GET` | `/api/medicines/{id}` | Get medicine by ID |
| `POST` | `/api/medicines` | Create a new medicine entry |
| `PUT` | `/api/medicines/{id}` | Update existing medicine details |
| `DELETE` | `/api/medicines/{id}` | Delete medicine record |
| `GET` | `/api/medicines/categories` | Retrieve distinct therapeutic categories |
| `GET` | `/api/history` | List previous analysis sessions |
| `GET` | `/api/history/{id}` | Get full details of a specific session |
| `DELETE` | `/api/history/{id}` | Delete a saved analysis session |
| `GET` | `/api/dashboard/stats` | Retrieve live dashboard aggregated statistics |
| `POST` | `/api/auth/register` | Register new user account |
| `POST` | `/api/auth/login` | Authenticate user & return JWT token |
| `GET` | `/api/auth/me` | Fetch authenticated user profile |
