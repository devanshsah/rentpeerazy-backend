#!/bin/bash

echo "====================================="
echo "RentPEasy Backend - Quick Test Script"
echo "====================================="
echo ""

BASE_URL="http://localhost:8001/api"

# Colors
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

echo -e "${YELLOW}Testing Backend Health...${NC}"
curl -s "$BASE_URL/../actuator/health" 2>/dev/null
if [ $? -eq 0 ]; then
    echo -e "${GREEN}✓ Backend is reachable${NC}"
else
    echo -e "${RED}✗ Backend is not reachable${NC}"
    echo "Make sure the backend is running on port 8001"
    exit 1
fi

echo -e "\n${YELLOW}1. Testing User Registration...${NC}"
REGISTER_RESPONSE=$(curl -s -X POST "$BASE_URL/auth/register" \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser'$(date +%s)'",
    "email": "test'$(date +%s)'@example.com",
    "password": "password123",
    "fullName": "Test User",
    "phoneNumber": "+91-9876543210"
  }')

if echo "$REGISTER_RESPONSE" | grep -q "accessToken"; then
    echo -e "${GREEN}✓ Registration successful${NC}"
    ACCESS_TOKEN=$(echo $REGISTER_RESPONSE | grep -o '"accessToken":"[^"]*' | sed 's/"accessToken":"//')
    echo "Access Token: ${ACCESS_TOKEN:0:50}..."
else
    echo -e "${RED}✗ Registration failed${NC}"
    echo "Response: $REGISTER_RESPONSE"
    exit 1
fi

echo -e "\n${YELLOW}2. Testing Property Creation...${NC}"
PROPERTY_RESPONSE=$(curl -s -X POST "$BASE_URL/properties" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $ACCESS_TOKEN" \
  -d '{
    "title": "Test Property",
    "description": "A beautiful test property",
    "type": "APARTMENT",
    "city": "Bangalore",
    "locality": "Koramangala",
    "price": 25000,
    "beds": 2,
    "baths": 2,
    "squareFeet": 1200,
    "contactNumber": "+91-9876543210",
    "images": ["https://example.com/image.jpg"],
    "amenities": ["Parking", "Gym"]
  }')

if echo "$PROPERTY_RESPONSE" | grep -q '"id"'; then
    echo -e "${GREEN}✓ Property created successfully${NC}"
    PROPERTY_ID=$(echo $PROPERTY_RESPONSE | grep -o '"id":"[^"]*' | sed 's/"id":"//')
    echo "Property ID: $PROPERTY_ID"
else
    echo -e "${RED}✗ Property creation failed${NC}"
    echo "Response: $PROPERTY_RESPONSE"
fi

echo -e "\n${YELLOW}3. Testing Get All Properties...${NC}"
ALL_PROPERTIES=$(curl -s -X GET "$BASE_URL/properties")

if echo "$ALL_PROPERTIES" | grep -q '"id"'; then
    echo -e "${GREEN}✓ Properties retrieved successfully${NC}"
    echo "Properties count: $(echo $ALL_PROPERTIES | grep -o '"id"' | wc -l)"
else
    echo -e "${RED}✗ Failed to retrieve properties${NC}"
fi

echo -e "\n${YELLOW}4. Testing Property Search (by city)...${NC}"
SEARCH_RESPONSE=$(curl -s -X GET "$BASE_URL/properties?city=Bangalore")

if echo "$SEARCH_RESPONSE" | grep -q 'Bangalore'; then
    echo -e "${GREEN}✓ Search successful${NC}"
else
    echo -e "${YELLOW}! No properties found in Bangalore${NC}"
fi

echo -e "\n${YELLOW}5. Testing Add to Favorites...${NC}"
if [ ! -z "$PROPERTY_ID" ]; then
    FAVORITE_RESPONSE=$(curl -s -X POST "$BASE_URL/favorites/$PROPERTY_ID" \
      -H "Authorization: Bearer $ACCESS_TOKEN")
    
    if echo "$FAVORITE_RESPONSE" | grep -q 'favorites'; then
        echo -e "${GREEN}✓ Property added to favorites${NC}"
    else
        echo -e "${RED}✗ Failed to add to favorites${NC}"
        echo "Response: $FAVORITE_RESPONSE"
    fi
else
    echo -e "${YELLOW}! Skipping - no property ID available${NC}"
fi

echo -e "\n${GREEN}=====================================${NC}"
echo -e "${GREEN}Testing Complete!${NC}"
echo -e "${GREEN}=====================================${NC}"