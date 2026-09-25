# Payflow

Payflow is a fintech transfer API built with Spring Boot. It provides functionalities for user authentication, wallet management, and secure fund transfers between users.

## Features

- **User Authentication**: Secure registration and login using JWT (JSON Web Tokens).
- **Wallet Management**: Each user gets a wallet upon registration. View balance and transaction history.
- **Fund Transfers**: Transfer funds between user wallets with atomicity and ledger tracking.
- **Media Upload**: Support for user avatars using Cloudinary.
- **Email Notifications**: Asynchronous email alerts for registration and transfers.
- **API Documentation**: Interactive API documentation powered by Swagger/OpenAPI.
- **Database Migrations**: Managed database schema using Flyway.

## Tech Stack

- **Java 25**
- **Spring Boot 4.1.1**
- **Spring Security & JWT**
- **Spring Data JPA**
- **PostgreSQL**
- **Flyway** (Migrations)
- **Cloudinary** (Media storage)
- **MapStruct** (Object mapping)
- **Lombok**
- **Docker Compose** (For local infrastructure)
- **Swagger UI** (API Documentation)

## Getting Started

### Prerequisites

- JDK 25 or higher
- Maven 3.9+
- Docker and Docker Compose (optional, for PostgreSQL and Mailpit)

### Configuration

1.  Clone the repository.
2.  Copy `.env.sample` to `.env` and fill in your actual credentials:
    ```bash
    cp .env.sample .env
    ```
3.  Update the values in `.env`:
    - `CLOUDINARY_CLOUD_NAME`: Your Cloudinary cloud name.
    - `CLOUDINARY_API_KEY`: Your Cloudinary API key.
    - `CLOUDINARY_API_SECRET`: Your Cloudinary API secret.
    - `JWT_SECRET`: A secure random string for JWT signing.
    - `JWT_EXPIRATION`: Token expiration time in milliseconds (e.g., 86400000 for 24 hours).

### Running with Docker

You can start the required infrastructure (PostgreSQL and Mailpit) using Docker Compose:

```bash
docker-compose up -d
```

### Running the Application

Use the Maven wrapper to run the application:

```bash
./mvnw spring-boot:run
```

The API will be available at `http://localhost:8080`.

## API Documentation

Once the application is running, you can access the Swagger UI at:
`http://localhost:8080/swagger-ui/index.html`

## Project Structure

- `src/main/java/com/example/payflow/controllers`: API endpoints.
- `src/main/java/com/example/payflow/service`: Business logic.
- `src/main/java/com/example/payflow/entity`: Database models.
- `src/main/java/com/example/payflow/repository`: Data access layer.
- `src/main/java/com/example/payflow/dto`: Data Transfer Objects.
- `src/main/resources/db/migration`: Flyway migration scripts.

## License

This project is licensed under the MIT License.
