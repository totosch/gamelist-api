# 🎮 Gamelist API

A REST API for managing a personal video game catalog, inspired by MyAnimeList. Users can browse games, add them to their personal list, and track their status and rating.

Live API: [gamelist-api-production-7e74.up.railway.app](https://gamelist-api-production-7e74.up.railway.app)  
Interactive docs: [Swagger UI](https://gamelist-api-production-7e74.up.railway.app/swagger-ui)

---

## Features

- Browse a catalog of games with genres and platforms
- User registration and authentication with JWT
- Personal game list with status tracking (Playing, Completed, Dropped, Pending)
- User ratings per game
- Search and filter games by title, genre, and platform
- Role-based access control (Admin / User)
- CI/CD pipeline with GitHub Actions and Railway

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 21 |
| Framework | Spring Boot 4 |
| Security | Spring Security + JWT |
| Persistence | Spring Data JPA + Hibernate |
| Database | PostgreSQL |
| Validation | Jakarta Validation |
| Documentation | SpringDoc OpenAPI (Swagger) |
| Containerization | Docker + Docker Compose |
| Deployment | Railway |
| CI/CD | GitHub Actions |
| Testing | JUnit 5 + Mockito |

---

## Project Structure

```
src/main/java/com/sch/gamelist_api/
├── config/          # Swagger configuration
├── controller/      # REST controllers
├── dto/             # Request and response objects
├── exception/       # Custom exceptions and global error handler
├── model/           # JPA entities
├── repository/      # Spring Data JPA repositories
├── security/        # JWT filter and security configuration
├── service/         # Business logic
└── specification/   # Dynamic query specifications
```

---

## API Endpoints

### Authentication
| Method | Endpoint | Description | Auth |
|---|---|---|---|
| POST | `/api/auth/register` | Register a new user | Public |
| POST | `/api/auth/login` | Login and receive JWT token | Public |

### Games
| Method | Endpoint | Description | Auth |
|---|---|---|---|
| GET | `/api/games` | List all games (supports filters) | Public |
| GET | `/api/games/{id}` | Get a game by ID | Public |
| POST | `/api/games` | Create a game | Admin |
| DELETE | `/api/games/{id}` | Delete a game | Admin |

### Genres & Platforms
| Method | Endpoint | Description | Auth |
|---|---|---|---|
| GET | `/api/genres` | List all genres | Public |
| POST | `/api/genres` | Create a genre | Admin |
| DELETE | `/api/genres/{id}` | Delete a genre | Admin |
| GET | `/api/platforms` | List all platforms | Public |
| POST | `/api/platforms` | Create a platform | Admin |
| DELETE | `/api/platforms/{id}` | Delete a platform | Admin |

### User Game List
| Method | Endpoint | Description | Auth |
|---|---|---|---|
| GET | `/api/user/games` | Get current user's game list | User |
| POST | `/api/user/games` | Add a game to the list | User |
| PUT | `/api/user/games/{gameId}` | Update status or rating | User |
| DELETE | `/api/user/games/{gameId}` | Remove a game from the list | User |

### Filters (query params on GET /api/games)
```
GET /api/games?title=zelda
GET /api/games?genreId=1
GET /api/games?platformId=2
GET /api/games?title=zelda&genreId=1
```

---

## Running Locally

### Prerequisites
- Java 21
- Docker and Docker Compose
- Maven (or use the included `mvnw` wrapper)

### Setup

1. Clone the repository:
```bash
git clone https://github.com/totosch/gamelist-api.git
cd gamelist-api
```

2. Create an `application.properties` file in `src/main/resources/` based on the provided example:
```bash
cp src/main/resources/application.properties.example src/main/resources/application.properties
```

3. Fill in your values in `application.properties`.

4. Create a `.env` file in the project root:
```
JWT_SECRET=your_secret_key_at_least_256_bits
JWT_EXPIRATION=86400000
```

5. Start the full stack with Docker Compose:
```bash
docker-compose up --build
```

The API will be available at `http://localhost:8080`.  
Swagger UI at `http://localhost:8080/swagger-ui`.

---

## Running Tests

```bash
./mvnw test
```

---

## CI/CD

Every push to `main` triggers the GitHub Actions pipeline which runs all tests. Railway only deploys if the pipeline passes.
