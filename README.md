# RentPEasy - Property Rental Platform

A full-stack property rental platform built with **Java Spring Boot**, **PostgreSQL**, **React**, and **Docker**.

## 🚀 Features

### Backend (Java Spring Boot)
- ✅ RESTful API with Spring Boot 3.2.5
- ✅ JWT Authentication (Access + Refresh tokens)
- ✅ PostgreSQL database with JPA/Hibernate
- ✅ Comprehensive property management
- ✅ User favorites/wishlist functionality
- ✅ Advanced search and filtering
- ✅ Role-based access control (USER, OWNER, ADMIN)
- ✅ Global exception handling
- ✅ Input validation

### Frontend (React)
- Property listings
- Search and filter functionality
- User authentication
- Favorites management

## 🏗️ Architecture

```
├── backend-java/          # Spring Boot Backend
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/rentpeeasy/backend/
│   │   │   │   ├── config/          # Security & App Configuration
│   │   │   │   ├── controller/      # REST Controllers
│   │   │   │   ├── dto/             # Data Transfer Objects
│   │   │   │   ├── entity/          # JPA Entities
│   │   │   │   ├── repository/      # JPA Repositories
│   │   │   │   ├── service/         # Business Logic
│   │   │   │   ├── security/        # JWT & Authentication
│   │   │   │   └── exception/       # Exception Handling
│   │   │   └── resources/
│   │   │       └── application.properties
│   │   └── test/
│   ├── Dockerfile
│   └── pom.xml
├── frontend/              # React Frontend
│   ├── src/
│   ├── Dockerfile
│   └── package.json
├── docker-compose.yml     # Docker orchestration
└── init.sql              # Database initialization
```

## 📋 Prerequisites

- Java 17 or higher
- Gradle 8.7+ (included as Gradle Wrapper)
- Node.js 18+ & Yarn
- Docker & Docker Compose
- PostgreSQL 16 (if running locally)

## 🛠️ Setup & Installation

### Option 1: Using Docker (Recommended)

1. **Clone the repository**
```bash
cd /app
```

2. **Build and run all services**
```bash
docker-compose up --build
```

This will start:
- PostgreSQL database on port 5432
- Spring Boot backend on port 8001
- React frontend on port 3000

3. **Access the application**
- Frontend: http://localhost:3000
- Backend API: http://localhost:8001/api

### Option 2: Local Development

#### Backend Setup

1. **Configure PostgreSQL**
```bash
# Create database
creatdb rentpeeasy

# Run initialization script
psql -d rentpeeasy -f init.sql
```

2. **Update application.properties**
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/rentpeeasy
spring.datasource.username=your_username
spring.datasource.password=your_password
```

3. **Build and run backend**
```bash
cd backend-java
mvn clean install
mvn spring-boot:run
```

Backend will run on http://localhost:8001

#### Frontend Setup

1. **Install dependencies**
```bash
cd frontend
yarn install
```

2. **Update .env file**
```bash
REACT_APP_BACKEND_URL=http://localhost:8001
```

3. **Run frontend**
```bash
yarn start
```

Frontend will run on http://localhost:3000

## 🔑 API Endpoints

### Authentication
```
POST   /api/auth/register    - Register new user
POST   /api/auth/login       - Login user
POST   /api/auth/refresh     - Refresh access token
POST   /api/auth/logout      - Logout user
```

### Properties
```
GET    /api/properties              - Get all properties (with optional filters)
GET    /api/properties/{id}         - Get property by ID
GET    /api/properties/featured     - Get featured properties
POST   /api/properties              - Create property (authenticated)
PUT    /api/properties/{id}         - Update property (owner only)
DELETE /api/properties/{id}         - Delete property (owner only)
```

**Query Parameters for search:**
- `city` - Filter by city
- `type` - Filter by property type (PG, ROOM, APARTMENT, FLAT, VILLA, COMMERCIAL)
- `minPrice` - Minimum price
- `maxPrice` - Maximum price

### Favorites
```
GET    /api/favorites              - Get user's favorites
POST   /api/favorites/{propertyId} - Add to favorites
DELETE /api/favorites/{propertyId} - Remove from favorites
```

## 🧪 Testing the API

### Register a new user
```bash
curl -X POST http://localhost:8001/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "email": "test@example.com",
    "password": "password123",
    "fullName": "Test User",
    "phoneNumber": "+91-9876543210"
  }'
```

### Login
```bash
curl -X POST http://localhost:8001/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "password123"
  }'
```

Save the `accessToken` from the response.

### Create a property
```bash
curl -X POST http://localhost:8001/api/properties \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_ACCESS_TOKEN" \
  -d '{
    "title": "Modern 2BHK Apartment",
    "description": "Beautiful apartment in prime location",
    "type": "APARTMENT",
    "city": "Bangalore",
    "locality": "Koramangala",
    "price": 25000,
    "beds": 2,
    "baths": 2,
    "squareFeet": 1200,
    "contactNumber": "+91-9876543210",
    "images": ["https://example.com/image1.jpg"],
    "amenities": ["Parking", "Gym", "Security"]
  }'
```

### Search properties
```bash
curl "http://localhost:8001/api/properties?city=Bangalore&type=APARTMENT&minPrice=20000&maxPrice=30000"
```

## 🔒 Security Features

- **JWT Authentication**: Secure token-based authentication
- **Password Encryption**: BCrypt password hashing
- **Role-based Access Control**: USER, OWNER, ADMIN roles
- **CORS Configuration**: Configurable allowed origins
- **Request Validation**: Input validation on all endpoints
- **Refresh Tokens**: Secure token refresh mechanism

## 🗄️ Database Schema

The database includes:
- **users** - User accounts with roles
- **properties** - Property listings
- **property_images** - Property images (one-to-many)
- **property_amenities** - Property amenities (one-to-many)
- **favorites** - User favorites/wishlist
- **refresh_tokens** - JWT refresh tokens

## 🐳 Docker Commands

```bash
# Start all services
docker-compose up -d

# View logs
docker-compose logs -f

# Stop all services
docker-compose down

# Rebuild and restart
docker-compose up --build

# Remove volumes (clean database)
docker-compose down -v
```

## 📝 Environment Variables

### Backend (.env)
```
DATABASE_URL=jdbc:postgresql://postgres:5432/rentpeeasy
DATABASE_USERNAME=postgres
DATABASE_PASSWORD=postgres123
JWT_SECRET=your-secret-key-here
CORS_ORIGINS=http://localhost:3000
```

### Frontend (.env)
```
REACT_APP_BACKEND_URL=http://localhost:8001
```

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Open a Pull Request

## 📄 License

MIT License

## 👥 Authors

RentPEasy Team

---

**Built with ❤️ using Java Spring Boot, PostgreSQL, React, and Docker**