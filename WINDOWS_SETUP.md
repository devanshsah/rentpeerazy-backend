# 🚀 Windows Local Development Setup (FASTEST!)

Since you're on Windows and Docker is taking forever, here's how to run **locally** (10x faster!).

---

## ⚡ Prerequisites Installation

### 1. **Java 17** (Required)

**Download & Install:**
- Go to: https://adoptium.net/
- Download: Eclipse Temurin 17 (LTS)
- Install with default options
- **Verify:**
  ```powershell
  java -version
  # Should show: openjdk version "17.x.x"
  ```

### 2. **PostgreSQL 16** (Required)

**Download & Install:**
- Go to: https://www.postgresql.org/download/windows/
- Download PostgreSQL 16 installer
- Install with:
  - Password: `postgres123` (remember this!)
  - Port: `5432`
  - Locale: Default

**Verify:**
```powershell
# Open PowerShell or Command Prompt
psql --version
# Should show: psql (PostgreSQL) 16.x
```

---

## 🗄️ Database Setup

### Step 1: Create Database

Open **SQL Shell (psql)** from Start Menu:

```sql
-- Login (press Enter for defaults, enter password when prompted)
Server [localhost]:  (press Enter)
Database [postgres]: (press Enter)
Port [5432]:        (press Enter)
Username [postgres]: (press Enter)
Password:           postgres123

-- Create database
CREATE DATABASE rentpeeasy;

-- Verify
\l
-- You should see rentpeeasy in the list

-- Exit
\q
```

---

## 🚀 Run the Application

### Option 1: Using PowerShell (Recommended)

Open PowerShell in your project directory:

```powershell
# Navigate to backend
cd E:\rentpeerazy-backend\backend-java

# First time: Fix gradlew line endings (Windows issue)
$gradlewContent = Get-Content .\gradlew -Raw
$gradlewContent = $gradlewContent -replace "`r`n", "`n"
[System.IO.File]::WriteAllText("$PWD\gradlew", $gradlewContent)

# Make sure you're in the right directory
pwd
# Should show: E:\rentpeerazy-backend\backend-java

# Update application.properties
# Edit: src\main\resources\application.properties
# Set:
# spring.datasource.url=jdbc:postgresql://localhost:5432/rentpeeasy
# spring.datasource.username=postgres
# spring.datasource.password=postgres123

# Run the application
.\gradlew.bat bootRun
```

**Expected Output:**
```
> Task :bootRun

  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::                (v3.2.5)

...
Started BackendApplication in 12.345 seconds
```

---

### Option 2: Using Command Prompt

```cmd
cd E:\rentpeerazy-backend\backend-java

REM Run the application
gradlew.bat bootRun
```

---

### Option 3: Using IntelliJ IDEA / Eclipse

**IntelliJ IDEA:**
1. Open IntelliJ IDEA
2. File → Open → Select `E:\rentpeerazy-backend\backend-java`
3. Wait for Gradle sync
4. Find `BackendApplication.java`
5. Right-click → Run 'BackendApplication'

**Eclipse:**
1. File → Import → Gradle → Existing Gradle Project
2. Select `E:\rentpeerazy-backend\backend-java`
3. Wait for import
4. Right-click project → Run As → Spring Boot App

---

## ✅ Verify It's Working

Open a new PowerShell/CMD window:

```powershell
# Test backend
curl http://localhost:8001/api/properties

# Should return: []
```

Or open browser: http://localhost:8001/api/properties

---

## 🐛 Troubleshooting

### Error: "gradlew is not recognized"

**Solution:**
```powershell
# Use gradlew.bat instead
.\gradlew.bat bootRun
```

### Error: "Connection refused to PostgreSQL"

**Solution 1:** Start PostgreSQL service
```powershell
# Open Services (Win + R, type: services.msc)
# Find "postgresql-x64-16"
# Right-click → Start
```

**Solution 2:** Or using PowerShell (as Administrator):
```powershell
Start-Service postgresql-x64-16
```

### Error: "database rentpeeasy does not exist"

**Solution:**
```powershell
# Open SQL Shell (psql)
# Login with postgres/postgres123
# Run:
CREATE DATABASE rentpeeasy;
```

### Error: Port 8001 already in use

**Solution:**
```powershell
# Find process using port 8001
netstat -ano | findstr :8001

# Kill the process (replace PID with actual PID)
taskkill /PID <PID> /F
```

### Error: "JAVA_HOME not set"

**Solution:**
```powershell
# Find Java installation
where java

# Set JAVA_HOME (replace path with your Java path)
[System.Environment]::SetEnvironmentVariable("JAVA_HOME", "C:\Program Files\Eclipse Adoptium\jdk-17.0.x-hotspot", "Machine")

# Restart PowerShell
```

---

## 📁 Project Structure

```
E:\rentpeerazy-backend\
├── backend-java\
│   ├── src\
│   ├── build.gradle
│   ├── gradlew           # Unix (won't work on Windows)
│   └── gradlew.bat       # Windows (use this!)
├── frontend\
└── docker-compose.yml    # (ignore for local development)
```

---

## 🔥 Hot Reload

**Already enabled!** Just save your Java files and Spring Boot DevTools will automatically reload in 2-3 seconds.

---

## 💡 Development Workflow

### Making Changes

1. **Edit Java file** in `src/main/java/`
2. **Save** (Ctrl+S)
3. **Wait 2-3 seconds** → Auto-reloads!
4. **Test** your changes

### Stopping the App

Press `Ctrl + C` in the terminal

### Restarting the App

```powershell
.\gradlew.bat bootRun
```

---

## 📊 Speed Comparison

| Method | Time |
|--------|------|
| **Docker build** | 8-10 minutes |
| **Local build** | 30-60 seconds |
| **Hot reload** | 2-3 seconds |

---

## 🎯 Recommended Setup

**For Daily Development:**
```powershell
# Start PostgreSQL (if not already running)
Start-Service postgresql-x64-16

# Start backend
cd E:\rentpeerazy-backend\backend-java
.\gradlew.bat bootRun

# In another terminal - Start frontend
cd E:\rentpeerazy-backend\frontend
yarn start
```

**Access:**
- Backend: http://localhost:8001/api
- Frontend: http://localhost:3000

---

## 🚀 Quick Start Script (Windows)

Save this as `start-local.bat`:

```batch
@echo off
echo ========================================
echo RentPEasy - Local Development
echo ========================================
echo.

echo Checking PostgreSQL...
net start | find "postgresql-x64-16" >nul
if errorlevel 1 (
    echo Starting PostgreSQL...
    net start postgresql-x64-16
) else (
    echo PostgreSQL is already running
)

echo.
echo Starting backend...
cd backend-java
.\gradlew.bat bootRun
```

Run it:
```powershell
.\start-local.bat
```

---

## ✅ Summary

**Installation (one-time):**
1. Install Java 17 ✓
2. Install PostgreSQL 16 ✓
3. Create database ✓

**Daily Usage:**
```powershell
cd E:\rentpeerazy-backend\backend-java
.\gradlew.bat bootRun
```

**Benefits:**
- ✅ Starts in 10-20 seconds
- ✅ Hot reload in 2-3 seconds
- ✅ No Docker overhead
- ✅ Easy debugging

---

## 🆘 Still Having Issues?

### Option 1: Use IntelliJ IDEA (Easiest!)
1. Download IntelliJ IDEA Community (Free)
2. Open project
3. Click green Run button
4. Done! ✓

### Option 2: Check Logs
```powershell
# Run with debug output
.\gradlew.bat bootRun --debug --stacktrace
```

---

**Local development is 10x faster than Docker on Windows! Give it a try! 🚀**
