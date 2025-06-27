# Provider Management REST API (Quarkus + Camel + Kotlin)

This project is a CRUD-based REST API for managing Providers using Quarkus, Apache Camel, Kotlin, and Hibernate ORM.

## Technologies Used

- Quarkus
- Apache Camel (platform-http, bean, jackson)
- Kotlin
- Hibernate ORM with Panache
- PostgreSQL (for persistence)

## How to Run (Dev Mode)

./mvnw quarkus:dev

Dev UI available at: http://localhost:8080/q/dev

## Build & Package

To build a runnable JAR:

./mvnw package

Run it:

java -jar target/quarkus-app/quarkus-run.jar

To create an über-jar:

./mvnw package -Dquarkus.package.jar.type=uber-jar

Run it:

java -jar target/*-runner.jar

To build a native executable (with GraalVM or container):

./mvnw package -Dnative

or

./mvnw package -Dnative -Dquarkus.native.container-build=true

## API Endpoints

Base URL: http://localhost:8080/providers/v1

| Method | Endpoint        | Description               |
|--------|------------------|---------------------------|
| GET    | /                | Get all providers         |
| GET    | /{id}            | Get provider by ID        |
| POST   | /                | Create new provider       |
| PUT    | /{id}            | Update provider by ID     |
| DELETE | /{id}            | Delete provider by ID     |

## Sample JSON Body for POST/PUT

{
  "name": "ExampleProvider",
  "logoUrl": "https://example.com/logo.png",
  "status": "ACTIVE",
  "sla": {
    "uptimePercent": 99.9,
    "deliveryTimeMs": 500
  }
}

## Error Responses (JSON Format)

Invalid or missing fields:

{
  "code": 400,
  "message": "Invalid input or missing required field"
}

Provider not found:

{
  "code": 404,
  "message": "Provider with ID 100 not found"
}

Duplicate provider:

{
  "code": 409,
  "message": "Provider with name 'ExampleProvider' already exists"
}

Internal server error:

{
  "code": 500,
  "message": "Unexpected error: <error message>"
}

## How to Test (Using Postman)

1. Run the app: ./mvnw quarkus:dev
2. Open Postman
3. Use base URL: http://localhost:8080/providers/v1
4. Select HTTP method (GET, POST, etc.)
5. Set header: Content-Type = application/json
6. Provide request body for POST/PUT
7. Observe JSON responses

## Input Validations

- Name must not be blank
- Name must be unique
- Status must be valid (e.g., ACTIVE)
- SLA fields (uptimePercent, deliveryTimeMs) must be valid numbers if provided

## Documentation Links

- Quarkus: https://quarkus.io/guides/
- Camel Quarkus: https://camel.apache.org/camel-quarkus/latest/
- Panache Kotlin ORM: https://quarkus.io/guides/hibernate-orm-panache-kotlin
