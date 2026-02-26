#!/bin/bash

echo "====================================="
echo "RentPEasy - Quick Start"
echo "====================================="
echo ""

# Check if Docker is installed
if ! command -v docker &> /dev/null; then
    echo "Error: Docker is not installed. Please install Docker first."
    exit 1
fi

# Check if Docker Compose is installed
if ! command -v docker-compose &> /dev/null; then
    echo "Error: Docker Compose is not installed. Please install Docker Compose first."
    exit 1
fi

echo "Starting RentPEasy application..."
echo ""

# Stop any existing containers
echo "Stopping existing containers..."
docker-compose down 2>/dev/null

# Build and start all services
echo "Building and starting services..."
docker-compose up --build -d

if [ $? -eq 0 ]; then
    echo ""
    echo "====================================="
    echo "RentPEasy is starting up!"
    echo "====================================="
    echo ""
    echo "Services:"
    echo "  - PostgreSQL:  localhost:5432"
    echo "  - Backend API: http://localhost:8001"
    echo "  - Frontend:    http://localhost:3000"
    echo ""
    echo "To view logs:"
    echo "  docker-compose logs -f"
    echo ""
    echo "To stop:"
    echo "  docker-compose down"
    echo ""
    echo "Waiting for services to be ready..."
    sleep 10
    
    echo ""
    echo "Application should be ready at:"
    echo "  http://localhost:3000"
    echo ""
else
    echo "Error: Failed to start services"
    exit 1
fi