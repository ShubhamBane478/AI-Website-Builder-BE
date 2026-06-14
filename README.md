# SiteForge — Backend API

Spring Boot 4 · Java 21 · PostgreSQL 15 · JWT Auth · Docker

---

## For Backend Developers

### Prerequisites
- Java 21
- Docker Desktop
- Maven (or use the included `./mvnw` wrapper)

### Run locally (native backend + Dockerised DB)

```bash
# 1. Start only the database in Docker
docker compose up -d postgres

# 2. Copy env file and fill in your values
cp .env.example .env

# 3. Run the Spring Boot app natively (IntelliJ or terminal)
./mvnw spring-boot:run
```

The API is available at **http://localhost:8080**.

### Run everything in Docker (backend + DB)

```bash
# Make sure you have pulled the latest backend image first
docker compose up -d
```

> **Note:** When running the backend inside Docker, `DB_URL` is automatically
> overridden by `docker-compose.yml` to use the Docker network hostname
> (`postgres`) instead of `localhost`.

---

### Build and Push the Docker image to Docker Hub

The `docker-publish.sh` script handles login → build → push → logout.
It reads credentials from environment variables so nothing is ever hardcoded.

```bash
# 1. Make the script executable (one-time setup)
chmod +x docker-publish.sh

# 2. Export your Docker Hub credentials
#    Use an Access Token (not your password):
#    hub.docker.com → Account Settings → Security → New Access Token
export DOCKER_USERNAME=shubhambane478
export DOCKER_PASSWORD=<your-docker-hub-access-token>

# 3. Publish (replace 1.0.0 with your version, or omit for :latest only)
./docker-publish.sh 1.0.0
```

The script will:
1. Log in to Docker Hub
2. Build the image tagged `:latest` and `:<version>`
3. Push both tags
4. Log out automatically

Published image: `shubhambane478/siteforge-backend`

---

## For Frontend Developers

You only need **Docker Desktop** — no Java, no Maven, no source code.

### Prerequisites
- [Docker Desktop](https://www.docker.com/products/docker-desktop)

### Quick start

**Step 1 — Get the two config files** from the backend developer (or the repo):
```
docker-compose.friend.yml
.env.example
```

**Step 2 — Create your local `.env`:**
```bash
cp .env.example .env
```
The default values work out of the box. No changes needed for local dev.

**Step 3 — Start both the database and the API:**
```bash
docker compose -f docker-compose.friend.yml up -d
```

The API is now running at **http://localhost:8080**. ✅

### Verify it's working

```bash
curl http://localhost:8080/api/health
# → {"status":"UP"}
```

### Stop everything

```bash
docker compose -f docker-compose.friend.yml down
```

---

## API Overview

| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| GET | `/api/health` | None | Health check |
| POST | `/api/v1/auth/register` | None | Register a new user |
| POST | `/api/v1/auth/login` | None | Login, get JWT *(coming soon)* |

Access token is returned in the response body.
Refresh token is set as an `httpOnly` cookie (7-day lifetime).
