# 🚀 Fast Local Development Setup (No Docker)

This guide shows you how to run RentPEasy **locally without Docker** for much faster development.

---

## ⚡ Why Run Locally?

**Docker Build Time**: 5-10 minutes  
**Local Build Time**: 30-60 seconds  

**Benefits**:
- ✅ Instant hot reload
- ✅ Faster builds (no container overhead)
- ✅ Direct debugging
- ✅ Less memory usage
- ✅ Easier IDE integration

---

## 📋 Prerequisites

1. **Java 17+**
   ```bash
   # Check version
   java -version
   
   # Download: https://adoptium.net/
   ```

2. **PostgreSQL 16**
   ```bash
   # Install PostgreSQL
   # macOS: brew install postgresql@16
   # Ubuntu: sudo apt install postgresql-16
   # Windows: Download from postgresql.org
   
   # Start PostgreSQL
   # macOS: brew services start postgresql@16
   # Ubuntu: sudo systemctl start postgresql
   # Windows: Start from Services
   ```

3. **Node.js 18+ & Yarn** (for frontend)
   ```bash
   node --version
   yarn --version
   ```

---

## 🗄️ Database Setup

### Step 1: Create Database
```bash
# Login to PostgreSQL
psql -U postgres

# Create database and user
CREATE DATABASE rentpeeasy;
CREATE USER rentpeeasy_user WITH PASSWORD 'rentpeeasy123';
GRANT ALL PRIVILEGES ON DATABASE rentpeeasy TO rentpeeasy_user;
\q
```

### Step 2: Update Configuration
Edit `/app/backend-java/src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/rentpeeasy
spring.datasource.username=rentpeeasy_user
spring.datasource.password=rentpeeasy123
```

**Note**: Liquibase will automatically create all tables on first run!

---

## 🏃 Running the Application

### Option 1: Using Gradle Wrapper (Recommended)

**Terminal 1 - Backend:**
```bash
cd /app/backend-java

# First time: Download dependencies
./gradlew dependencies

# Run the application
./gradlew bootRun

# Or with live reload
./gradlew bootRun --continuous
```

**Terminal 2 - Frontend:**
```bash
cd /app/frontend

# Install dependencies
yarn install

# Run frontend
yarn start
```

---

### Option 2: Build JAR and Run

```bash
cd /app/backend-java

# Build JAR
./gradlew clean bootJar

# Run JAR
java -jar build/libs/app.jar
```

---

## 🔥 Hot Reload Setup

### Backend Hot Reload
Spring Boot DevTools is already included! Just run:
```bash
./gradlew bootRun
```

Any changes to Java files will auto-reload in ~2-3 seconds.

### Frontend Hot Reload
React automatically hot reloads when you run:
```bash
yarn start
```

---

## ✅ Verify Setup

### 1. Check Backend
```bash
# Test health
curl http://localhost:8001/api/properties

# Should return: []
```

### 2. Check Database
```bash
# Connect to database
psql -U rentpeeasy_user -d rentpeeasy

# List tables (created by Liquibase)
\dt

# Should see: users, properties, favorites, refresh_tokens, etc.
\q
```

### 3. Test Registration
```bash
curl -X POST http://localhost:8001/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "email": "test@example.com",
    "password": "password123",
    "fullName": "Test User"
  }'
```

---

## 📊 Build Times Comparison

| Method | First Build | Rebuild | Hot Reload |
|--------|-------------|---------|------------|
| **Docker** | 8-10 min | 5-8 min | Not available |
| **Local Gradle** | 1-2 min | 30 sec | 2-3 sec |

---

## 🛠️ Development Workflow

### Making Changes

1. **Edit Java code** → Auto-reloads in 2-3 seconds
2. **Edit properties** → Restart required (30 sec rebuild)
3. **Add dependencies** → Edit `build.gradle` → Restart

### Database Migrations

**Adding a new migration:**

1. Create new file: `db/changelog/changes/006-your-migration.yaml`
2. Add to master: `db/changelog/db.changelog-master.yaml`
3. Restart app → Liquibase auto-applies

Example:
```yaml
# db/changelog/changes/006-add-reviews-table.yaml
databaseChangeLog:
  - changeSet:
      id: 006-add-reviews-table
      author: yourname
      changes:
        - createTable:
            tableName: reviews
            columns:
              - column:
                  name: id
                  type: uuid
                  constraints:
                    primaryKey: true
```

---

## 🐛 Troubleshooting

### Port Already in Use
```bash
# Find process
lsof -i :8001
# Kill it
kill -9 <PID>
```

### Database Connection Issues
```bash
# Check if PostgreSQL is running
pg_isready

# Check connection
psql -U rentpeeasy_user -d rentpeeasy
```

### Gradle Issues
```bash
# Clean build
./gradlew clean build --refresh-dependencies

# Check Gradle version
./gradlew --version
```

### Liquibase Issues
```bash
# Check migration status
./gradlew liquibaseStatus

# Rollback last migration
./gradlew liquibaseRollbackCount -PliquibaseCommandValue=1
```

---

## 🎯 Common Commands

```bash
# Build without running
./gradlew build

# Build without tests
./gradlew build -x test

# Clean build
./gradlew clean build

# Check dependencies
./gradlew dependencies

# Run tests
./gradlew test

# Generate JAR
./gradlew bootJar

# Check for updates
./gradlew dependencyUpdates
```

---

## 📁 Project Structure

```
backend-java/
├── src/
│   ├── main/
│   │   ├── java/          # Java source code
│   │   └── resources/
│   │       ├── application.properties
│   │       └── db/changelog/  # Liquibase migrations
│   └── test/
├── build.gradle           # Dependencies
├── gradlew               # Gradle wrapper (Unix)
└── gradlew.bat           # Gradle wrapper (Windows)
```

---

## 💡 Pro Tips

1. **Use IDE for faster development**
   - IntelliJ IDEA / Eclipse
   - Import as Gradle project
   - Use built-in debugger

2. **Database GUI Tools**
   - DBeaver (Free)
   - pgAdmin
   - DataGrip (Paid)

3. **API Testing**
   - Use Postman or Insomnia
   - Import OpenAPI spec
   - Save requests for reuse

4. **Logs**
   ```bash
   # Watch logs in real-time
   tail -f logs/spring.log
   ```

---

## 🐳 When to Use Docker?

Use Docker for:
- ✅ Production deployment
- ✅ CI/CD pipelines
- ✅ Team consistency
- ✅ Demo/presentation

Use Local Setup for:
- ✅ Active development
- ✅ Debugging
- ✅ Quick iterations
- ✅ Testing changes

---

## 🔄 Switch Between Local and Docker

You can easily switch:

**From Local to Docker:**
```bash
# Stop local app (Ctrl+C)
docker-compose up
```

**From Docker to Local:**
```bash
# Stop Docker
docker-compose down

# Start local
./gradlew bootRun
```

---

## ✨ Summary

**Local Development** is **10x faster** than Docker for active development!

1. Setup time: 5 minutes
2. Build time: 30 seconds
3. Hot reload: 2-3 seconds
4. Full control over debugging

Use Docker only when you need containerization or deployment.

---

**Happy Coding! 🚀**
