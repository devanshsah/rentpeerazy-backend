# 🐳 Docker-Only Setup - FIXED!

## ✅ What Was Fixed

**Problem:** `gradle-wrapper.jar` missing/corrupted  
**Solution:** Use official Gradle Docker image (has Gradle pre-installed)

---

## 🚀 Quick Start (Docker Only)

### Step 1: Build and Run

Open PowerShell in `E:\rentpeerazy-backend`:

```powershell
# Build and start all services
docker-compose up --build
```

**Expected time:** 5-8 minutes (first time)

---

## 📋 What's Running

After `docker-compose up`, you'll have:

1. **PostgreSQL** - Port 5432
2. **Backend** - Port 8001
3. **Frontend** - Port 3000

**Access:**
- Backend API: http://localhost:8001/api
- Frontend: http://localhost:3000

---

## 🔍 Monitor Progress

While building, watch the output:

```
[+] Building...
 => [backend] FROM gradle:8.7-jdk17-alpine
 => [backend] COPY build.gradle settings.gradle
 => [backend] COPY src ./src
 => [backend] RUN gradle clean bootJar
 => [backend] Exporting layers...
```

---

## ✅ Verify It's Working

Open another PowerShell window:

```powershell
# Check running containers
docker-compose ps

# Should show:
# NAME                STATUS
# rentpeeasy-postgres   Up
# rentpeeasy-backend    Up
# rentpeeasy-frontend   Up

# Test backend
curl http://localhost:8001/api/properties

# Or open in browser
start http://localhost:8001/api/properties
```

---

## 🛠️ Docker Commands

### Start Services
```powershell
docker-compose up
# Or in background:
docker-compose up -d
```

### Stop Services
```powershell
# Graceful stop
docker-compose stop

# Or stop and remove
docker-compose down
```

### View Logs
```powershell
# All logs
docker-compose logs -f

# Backend only
docker-compose logs -f backend

# Frontend only
docker-compose logs -f frontend

# PostgreSQL only
docker-compose logs -f postgres
```

### Rebuild After Code Changes
```powershell
# Rebuild and restart
docker-compose up --build

# Rebuild specific service
docker-compose build backend
docker-compose up backend
```

### Clean Everything
```powershell
# Stop and remove containers
docker-compose down

# Remove volumes too (deletes database!)
docker-compose down -v

# Clean build cache
docker system prune -a
```

---

## 🐛 Troubleshooting

### Issue: Port Already in Use

**Error:** `port is already allocated`

**Solution:**
```powershell
# Find what's using the port
netstat -ano | findstr :8001

# Kill the process
taskkill /PID <PID> /F

# Or use different ports in docker-compose.yml
# Change: "8001:8001" to "8002:8001"
```

---

### Issue: Build Takes Too Long

**Problem:** Downloads taking forever

**Solutions:**

1. **Check internet connection**
2. **Use Docker cache:**
   ```powershell
   # Don't use --build if nothing changed
   docker-compose up
   ```

3. **Increase Docker resources:**
   - Open Docker Desktop
   - Settings → Resources
   - Increase CPU: 4+ cores
   - Increase Memory: 4GB+

---

### Issue: Backend Container Keeps Restarting

**Check logs:**
```powershell
docker-compose logs backend
```

**Common causes:**

1. **Database not ready:**
   - Wait 30 seconds for PostgreSQL to initialize
   - Check: `docker-compose logs postgres`

2. **Liquibase migration failed:**
   - Check logs for Liquibase errors
   - Restart: `docker-compose restart backend`

3. **Port conflict:**
   - Change port in docker-compose.yml

---

### Issue: "No space left on device"

**Solution:**
```powershell
# Clean Docker
docker system prune -a --volumes

# This will remove:
# - All stopped containers
# - All unused images
# - All unused volumes
```

---

### Issue: Changes Not Reflected

**Problem:** Made code changes but nothing changed

**Solution:**
```powershell
# You MUST rebuild after code changes
docker-compose down
docker-compose up --build
```

**Note:** Docker doesn't have hot reload. Every change needs rebuild (5-8 min)

---

## 📊 Docker Resource Usage

**Expected usage:**
- CPU: 50-80% during build, 10-20% when running
- RAM: 2-3GB
- Disk: ~5GB for images

**Check usage:**
```powershell
docker stats
```

---

## 🎯 Development Workflow with Docker

### Making Changes

1. **Edit code** in your IDE
2. **Save changes**
3. **Stop Docker:**
   ```powershell
   docker-compose down
   ```
4. **Rebuild:**
   ```powershell
   docker-compose up --build
   ```
5. **Wait 5-8 minutes** 😅
6. **Test changes**

### Testing

```powershell
# Run test script
.\test-backend.sh

# Or manual tests
curl http://localhost:8001/api/auth/register `
  -Method POST `
  -ContentType "application/json" `
  -Body '{"username":"test","email":"test@example.com","password":"pass123"}'
```

---

## 📁 Docker Files

```
E:\rentpeerazy-backend\
├── docker-compose.yml          # Orchestrates all services
├── backend-java\
│   ├── Dockerfile              # Backend image config
│   └── .dockerignore          # Files to exclude
├── frontend\
│   └── Dockerfile              # Frontend image config
└── init.sql                    # Database initialization
```

---

## 🔧 Customizing Docker Setup

### Change Ports

Edit `docker-compose.yml`:

```yaml
services:
  backend:
    ports:
      - "8002:8001"  # Change 8001 to 8002

  frontend:
    ports:
      - "3001:3000"  # Change 3000 to 3001
```

### Add Environment Variables

Edit `docker-compose.yml`:

```yaml
services:
  backend:
    environment:
      - SPRING_PROFILES_ACTIVE=dev
      - CUSTOM_VAR=value
```

### Mount Code for Development

Edit `docker-compose.yml` (NOT recommended, very slow on Windows):

```yaml
services:
  backend:
    volumes:
      - ./backend-java/src:/app/src
```

---

## ⚡ Speed Optimization Tips

### 1. Use BuildKit
```powershell
# Enable BuildKit (faster builds)
$env:DOCKER_BUILDKIT=1
docker-compose build
```

### 2. Layer Caching
The Dockerfile is already optimized with layer caching:
- Dependencies downloaded first (cached)
- Code copied last (changes often)

### 3. Don't Rebuild Unnecessarily
```powershell
# If no code changes, just start:
docker-compose up

# Only rebuild when code changed:
docker-compose up --build
```

### 4. Build Specific Service
```powershell
# Only rebuild backend
docker-compose build backend
docker-compose up
```

---

## 🎓 Docker Compose Commands Reference

```powershell
# Start
docker-compose up                 # Foreground
docker-compose up -d              # Background

# Stop
docker-compose stop              # Stop containers
docker-compose down              # Stop and remove

# Build
docker-compose build             # Build all
docker-compose build backend     # Build specific service

# Logs
docker-compose logs              # All logs
docker-compose logs -f           # Follow logs
docker-compose logs backend      # Specific service

# Status
docker-compose ps                # Running containers
docker-compose top               # Processes

# Execute Commands
docker-compose exec backend sh   # Shell in backend container
docker-compose exec postgres psql -U postgres  # PostgreSQL shell

# Clean
docker-compose down -v           # Remove volumes
docker-compose down --rmi all    # Remove images too
```

---

## 📊 Build Time Breakdown

| Phase | Time | Description |
|-------|------|-------------|
| Download base images | 1-2 min | First time only |
| Download Gradle deps | 2-3 min | Cached after first build |
| Compile Java code | 1-2 min | Every build |
| Package JAR | 30 sec | Every build |
| Build frontend | 1-2 min | If changed |
| **Total** | **5-8 min** | Full rebuild |

---

## ✅ Verification Checklist

After `docker-compose up`, verify:

```powershell
# 1. Check all containers running
docker-compose ps
# Should show 3 services: postgres, backend, frontend

# 2. Test PostgreSQL
docker-compose exec postgres psql -U postgres -d rentpeeasy -c "\dt"
# Should list tables: users, properties, favorites, etc.

# 3. Test Backend
curl http://localhost:8001/api/properties
# Should return: []

# 4. Test Frontend
start http://localhost:3000
# Should open React app
```

---

## 🎯 Summary

✅ **Fixed:** Gradle wrapper issue  
✅ **Using:** Official Gradle image  
✅ **Optimized:** Docker layer caching  
✅ **Ready:** Just run `docker-compose up --build`  

---

## 🚀 Final Commands

```powershell
# Navigate to project
cd E:\rentpeerazy-backend

# Start everything (first time: 8-10 min)
docker-compose up --build

# Access:
# - Backend: http://localhost:8001/api
# - Frontend: http://localhost:3000
# - Database: localhost:5432

# Stop (Ctrl+C, then):
docker-compose down
```

---

**Docker is now fixed and ready to use! 🎉**

**Build time:** 5-8 minutes  
**Hot reload:** ❌ No (need to rebuild)  
**Memory:** ~2GB  
**Ports:** 3000, 5432, 8001  

---

**Pro tip:** If you get frustrated with slow Docker builds during development, local setup is still 10x faster! See `WINDOWS_SETUP.md` 😉
