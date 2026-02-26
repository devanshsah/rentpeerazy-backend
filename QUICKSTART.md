# RentPEasy - Quick Start Guide

## 🚀 Quick Start with Docker

### Prerequisites
- Docker Desktop installed
- Docker Compose installed
- Ports 3000, 5432, and 8001 available

### Start the Application

1. **Run the quick start script:**
```bash
chmod +x start.sh
./start.sh
```

2. **Or manually with Docker Compose:**
```bash
docker-compose up --build
```

3. **Access the application:**
- Frontend: http://localhost:3000
- Backend API: http://localhost:8001/api
- PostgreSQL: localhost:5432

### Test the Backend

Run the test script:
```bash
chmod +x test-backend.sh
./test-backend.sh
```

## 🛠️ Manual Setup (Without Docker)

### 1. Database Setup

```bash
# Install PostgreSQL 16
# Create database
createdb rentpeeasy

# Run initialization script
psql -d rentpeeasy -f init.sql
```

### 2. Backend Setup (Java Spring Boot)

```bash
cd backend-java

# Update application.properties with your database credentials
# Build the project
mvn clean install

# Run the application
mvn spring-boot:run
```

Backend will start on http://localhost:8001

### 3. Frontend Setup (React)

```bash
cd frontend

# Install dependencies
yarn install

# Create .env file
echo "REACT_APP_BACKEND_URL=http://localhost:8001" > .env

# Start development server
yarn start
```

Frontend will start on http://localhost:3000

## 🧪 Testing API Endpoints

### 1. Register a User

```bash
curl -X POST http://localhost:8001/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john_doe",
    "email": "john@example.com",
    "password": "password123",
    "fullName": "John Doe",
    "phoneNumber": "+91-9876543210"
  }'
```

**Response:**
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "refreshToken": "f47ac10b-58cc-4372-a567-0e02b2c3d479",
  "tokenType": "Bearer",
  "user": {
    "id": "123e4567-e89b-12d3-a456-426614174000",
    "username": "john_doe",
    "email": "john@example.com",
    "fullName": "John Doe",
    "role": "USER"
  }
}
```

### 2. Login

```bash
curl -X POST http://localhost:8001/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john_doe",
    "password": "password123"
  }'
```

### 3. Create a Property (Authenticated)

```bash
# Replace YOUR_ACCESS_TOKEN with the token from login response
curl -X POST http://localhost:8001/api/properties \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_ACCESS_TOKEN" \
  -d '{
    "title": "Modern 2BHK Apartment in Koramangala",
    "description": "Beautiful apartment with modern amenities",
    "type": "APARTMENT",
    "city": "Bangalore",
    "locality": "Koramangala",
    "price": 25000,
    "beds": 2,
    "baths": 2,
    "squareFeet": 1200,
    "contactNumber": "+91-9876543210",
    "images": [
      "https://images.unsplash.com/photo-1545324418-cc1a3fa10c00?w=400"
    ],
    "amenities": ["Parking", "Gym", "Security", "Power Backup"]
  }'
```

### 4. Get All Properties

```bash
curl http://localhost:8001/api/properties
```

### 5. Search Properties

```bash
# Search by city
curl "http://localhost:8001/api/properties?city=Bangalore"

# Search by type
curl "http://localhost:8001/api/properties?type=APARTMENT"

# Search with price range
curl "http://localhost:8001/api/properties?minPrice=20000&maxPrice=30000"

# Combined filters
curl "http://localhost:8001/api/properties?city=Bangalore&type=APARTMENT&minPrice=20000&maxPrice=30000"
```

### 6. Get Featured Properties

```bash
curl http://localhost:8001/api/properties/featured
```

### 7. Add to Favorites (Authenticated)

```bash
# Replace PROPERTY_ID and YOUR_ACCESS_TOKEN
curl -X POST http://localhost:8001/api/favorites/PROPERTY_ID \
  -H "Authorization: Bearer YOUR_ACCESS_TOKEN"
```

### 8. Get User Favorites (Authenticated)

```bash
curl http://localhost:8001/api/favorites \
  -H "Authorization: Bearer YOUR_ACCESS_TOKEN"
```

### 9. Refresh Access Token

```bash
curl -X POST http://localhost:8001/api/auth/refresh \
  -H "Content-Type: application/json" \
  -d '{
    "refreshToken": "YOUR_REFRESH_TOKEN"
  }'
```

### 10. Logout

```bash
curl -X POST http://localhost:8001/api/auth/logout \
  -H "Authorization: Bearer YOUR_ACCESS_TOKEN"
```

## 🐞 Troubleshooting

### Port Already in Use

```bash
# Check what's using the port
lsof -i :8001
lsof -i :3000
lsof -i :5432

# Kill the process
kill -9 <PID>
```

### Docker Issues

```bash
# Stop all containers
docker-compose down

# Remove volumes (cleans database)
docker-compose down -v

# Rebuild from scratch
docker-compose build --no-cache
docker-compose up
```

### Backend Not Starting

```bash
# Check logs
docker-compose logs backend

# Or if running locally
cd backend-java
mvn clean install -X
```

### Database Connection Issues

```bash
# Test PostgreSQL connection
psql -h localhost -U postgres -d rentpeeasy

# Check if PostgreSQL is running
docker-compose ps
```

## 📊 Project Structure

```
rentpeeasy/
├── backend-java/           # Spring Boot Backend
│   ├── src/main/java/      # Java source code
│   ├── src/main/resources/ # Configuration files
│   ├── Dockerfile          # Backend Docker image
│   └── pom.xml             # Maven dependencies
├── frontend/              # React Frontend
│   ├── src/                # React source code
│   ├── Dockerfile          # Frontend Docker image
│   └── package.json        # Node dependencies
├── docker-compose.yml     # Multi-container setup
├── init.sql              # Database schema
├── start.sh              # Quick start script
├── test-backend.sh       # API test script
└── README.md             # Full documentation
```

## 🔑 Default Credentials (for testing)

After running `init.sql`, you can use:
- Username: `admin`
- Email: `admin@rentpeeasy.com`
- Password: (hash provided, create a new user instead)

## 📝 Environment Variables

### Backend
- `DATABASE_URL`: PostgreSQL connection URL
- `DATABASE_USERNAME`: Database username
- `DATABASE_PASSWORD`: Database password
- `JWT_SECRET`: Secret key for JWT signing
- `CORS_ORIGINS`: Allowed origins for CORS

### Frontend
- `REACT_APP_BACKEND_URL`: Backend API URL

## 🚀 Deployment

### Docker Hub

```bash
# Build images
docker build -t rentpeeasy-backend:latest ./backend-java
docker build -t rentpeeasy-frontend:latest ./frontend

# Push to registry
docker push rentpeeasy-backend:latest
docker push rentpeeasy-frontend:latest
```

### Kubernetes

Create deployment manifests for:
- PostgreSQL StatefulSet
- Backend Deployment
- Frontend Deployment
- Services and Ingress

---

**Need help? Check the full README.md or raise an issue!**