#!/bin/bash

echo "========================================="
echo "RentPEasy - Local Development Startup"
echo "========================================="
echo ""

# Colors
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Check Java
echo -e "${YELLOW}Checking prerequisites...${NC}"
if ! command -v java &> /dev/null; then
    echo -e "${RED}✗ Java not found. Please install Java 17+${NC}"
    exit 1
fi
echo -e "${GREEN}✓ Java found: $(java -version 2>&1 | head -1)${NC}"

# Check PostgreSQL
if ! command -v psql &> /dev/null; then
    echo -e "${RED}✗ PostgreSQL not found. Please install PostgreSQL 16${NC}"
    echo "  macOS: brew install postgresql@16"
    echo "  Ubuntu: sudo apt install postgresql-16"
    exit 1
fi
echo -e "${GREEN}✓ PostgreSQL found${NC}"

# Check if database exists
if psql -U postgres -lqt | cut -d \| -f 1 | grep -qw rentpeeasy; then
    echo -e "${GREEN}✓ Database 'rentpeeasy' exists${NC}"
else
    echo -e "${YELLOW}! Database 'rentpeeasy' not found${NC}"
    echo -e "${YELLOW}Creating database...${NC}"
    psql -U postgres -c "CREATE DATABASE rentpeeasy;" 2>/dev/null
    if [ $? -eq 0 ]; then
        echo -e "${GREEN}✓ Database created${NC}"
    else
        echo -e "${RED}✗ Failed to create database. Please create manually:${NC}"
        echo "  psql -U postgres"
        echo "  CREATE DATABASE rentpeeasy;"
        exit 1
    fi
fi

echo ""
echo -e "${YELLOW}Starting backend...${NC}"
cd /app/backend-java

# Check if first run (no build directory)
if [ ! -d "build" ]; then
    echo -e "${YELLOW}First run detected. Downloading dependencies...${NC}"
    ./gradlew dependencies --no-daemon
fi

echo -e "${GREEN}Starting Spring Boot application...${NC}"
echo -e "${GREEN}Backend will be available at: http://localhost:8001${NC}"
echo -e "${GREEN}Press Ctrl+C to stop${NC}"
echo ""

./gradlew bootRun
