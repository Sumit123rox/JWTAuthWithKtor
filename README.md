# JWTAuthWithKtor

## Features

- **JWT Authentication**: Protect API endpoints using JWT tokens.
- **User Registration & Login**: Users can register and log in to obtain a JWT token.
- **Protected Endpoints**: API routes require a valid JWT token for access.

## Prerequisites

- Kotlin 1.5+
- Ktor Framework
- Gradle

## Setup

1. Clone this repository:
   ```bash
   git clone https://github.com/yourusername/JWTAuthWithKtor.git
   ```
2. Navigate to the project directory:
   ```bash
   cd JWTAuthWithKtor
   ```
3. Configure JWT settings in `application.conf`:
   ```hocon
   jwt {
       secret = "your_secret_key"
       issuer = "your_issuer"
       audience = "your_audience"
       realm = "your_realm"
   }
   ```
4. Run the project using Gradle:
   ```bash
   ./gradlew run
   ```

## Usage

- **Register**: Send a POST request to `/register` with user credentials.
- **Login**: Send a POST request to `/login` to receive a JWT token.
- **Access Protected Routes**: Send the JWT token as a Bearer token in the `Authorization` header to access secured endpoints.

## Endpoints

- `POST /register` - Register a new user.
- `POST /login` - Authenticate user and get JWT.
- `GET /protected` - Access protected endpoint (JWT required).
