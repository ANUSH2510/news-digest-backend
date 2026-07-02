# AI News Digest - Backend

A Spring Boot REST API that fetches real-time news, generates AI summaries, and serves personalized news digests.

## Features
- JWT Authentication (signup/login)
- NewsAPI integration for real-time news fetching
- Groq LLaMA AI summarization
- Personalized feed based on user topic preferences
- Scheduled daily news refresh
- PostgreSQL database

## Tech Stack
- Java 17
- Spring Boot 3.5.1
- Spring Security + JWT
- Spring Data JPA + PostgreSQL
- Groq AI API (LLaMA 3.3 70B)
- NewsAPI

## Setup
1. Clone the repo
2. Copy `application.properties.example` to `application.properties`
3. Fill in your environment variables
4. Run with IntelliJ or `./mvnw spring-boot:run`

## API Endpoints
- `POST /api/auth/signup` - Register
- `POST /api/auth/login` - Login
- `GET /api/topics` - Get all topics
- `POST /api/topics/select` - Select user topics
- `GET /api/digest` - Get personalized digest

## Deployment
Backend deployed on Railway with PostgreSQL.
