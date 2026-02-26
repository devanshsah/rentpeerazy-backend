@echo off
echo ========================================
echo RentPEasy - Local Development (Windows)
echo ========================================
echo.

REM Check if PostgreSQL is running
echo Checking PostgreSQL...
sc query postgresql-x64-16 | find "RUNNING" >nul
if errorlevel 1 (
    echo PostgreSQL is not running. Starting...
    net start postgresql-x64-16 2>nul
    if errorlevel 1 (
        echo Warning: Could not start PostgreSQL automatically.
        echo Please start it manually from Services or pgAdmin
        pause
    )
) else (
    echo PostgreSQL is running
)

echo.
echo Starting backend on http://localhost:8001
echo Press Ctrl+C to stop
echo.

cd backend-java
gradlew.bat bootRun
