# RentPEasy - System Architecture

```
┌─────────────────────────────────────────────────────────────────────────┐
│                         CLIENT LAYER (Browser)                          │
│                                                                          │
│  ┌──────────────────────────────────────────────────────────────┐      │
│  │                    React Frontend (Port 3000)                 │      │
│  │  ┌────────────┬────────────┬────────────┬────────────┐      │      │
│  │  │   Auth     │ Properties │  Search    │ Favorites  │      │      │
│  │  │   Pages    │   Pages    │  & Filter  │   Pages    │      │      │
│  │  └────────────┴────────────┴────────────┴────────────┘      │      │
│  │                         │                                     │      │
│  │                    Axios HTTP Client                          │      │
│  └─────────────────────────┼─────────────────────────────────────┘      │
└─────────────────────────────┼──────────────────────────────────────────┘
                              │
                              │ HTTP/REST API
                              │ (JWT Bearer Token)
                              ▼
┌─────────────────────────────────────────────────────────────────────────┐
│                      APPLICATION LAYER (Backend)                         │
│                   Spring Boot 3.2.5 (Port 8001)                         │
│                                                                          │
│  ┌──────────────────────────────────────────────────────────────┐      │
│  │                    Security Layer                             │      │
│  │  ┌────────────────────────────────────────────────┐          │      │
│  │  │   JwtAuthenticationFilter                      │          │      │
│  │  │   • Validates JWT tokens                       │          │      │
│  │  │   • Sets authentication context                │          │      │
│  │  └────────────────────────────────────────────────┘          │      │
│  │  ┌────────────────────────────────────────────────┐          │      │
│  │  │   SecurityConfig                               │          │      │
│  │  │   • CORS configuration                         │          │      │
│  │  │   • Role-based access control                  │          │      │
│  │  │   • BCrypt password encoding                   │          │      │
│  │  └────────────────────────────────────────────────┘          │      │
│  └──────────────────────────────────────────────────────────────┘      │
│                              │                                           │
│  ┌──────────────────────────▼───────────────────────────────────┐      │
│  │                    REST Controllers                           │      │
│  │  ┌──────────┐  ┌──────────────┐  ┌───────────────┐          │      │
│  │  │  Auth    │  │  Property    │  │   Favorite    │          │      │
│  │  │Controller│  │  Controller  │  │  Controller   │          │      │
│  │  └────┬─────┘  └──────┬───────┘  └───────┬───────┘          │      │
│  └───────┼────────────────┼──────────────────┼──────────────────┘      │
│          │                │                  │                          │
│  ┌───────▼────────────────▼──────────────────▼──────────────────┐      │
│  │                    Service Layer                              │      │
│  │  ┌──────────┐  ┌──────────────┐  ┌───────────────┐          │      │
│  │  │  Auth    │  │  Property    │  │   Favorite    │          │      │
│  │  │ Service  │  │   Service    │  │   Service     │          │      │
│  │  │          │  │              │  │               │          │      │
│  │  │ • JWT    │  │ • CRUD ops   │  │ • Add/Remove  │          │      │
│  │  │ • Login  │  │ • Search     │  │ • Get list    │          │      │
│  │  │ • Logout │  │ • Filter     │  │               │          │      │
│  │  └────┬─────┘  └──────┬───────┘  └───────┬───────┘          │      │
│  └───────┼────────────────┼──────────────────┼──────────────────┘      │
│          │                │                  │                          │
│  ┌───────▼────────────────▼──────────────────▼──────────────────┐      │
│  │                  Repository Layer (JPA)                       │      │
│  │  ┌──────────┐  ┌──────────────┐  ┌───────────────┐          │      │
│  │  │   User   │  │  Property    │  │   Favorite    │          │      │
│  │  │   Repo   │  │    Repo      │  │     Repo      │          │      │
│  │  └────┬─────┘  └──────┬───────┘  └───────┬───────┘          │      │
│  └───────┼────────────────┼──────────────────┼──────────────────┘      │
│          │                │                  │                          │
└──────────┼────────────────┼──────────────────┼───────────────────────────┘
           │                │                  │
           │ Hibernate/JPA  │                  │
           │                │                  │
           ▼                ▼                  ▼
┌─────────────────────────────────────────────────────────────────────────┐
│                      DATA LAYER (PostgreSQL 16)                          │
│                           Port 5432                                      │
│                                                                          │
│  ┌──────────────────────────────────────────────────────────────┐      │
│  │                       Database Schema                         │      │
│  │                                                               │      │
│  │  ┌─────────┐    ┌────────────┐    ┌──────────┐             │      │
│  │  │  users  │───▶│ properties │◀───│ favorites│             │      │
│  │  │         │    │            │    │          │             │      │
│  │  │ • id    │    │ • id       │    │ • user_id│             │      │
│  │  │ • user  │    │ • title    │    │ • prop_id│             │      │
│  │  │ • email │    │ • type     │    └──────────┘             │      │
│  │  │ • pwd   │    │ • city     │                              │      │
│  │  │ • role  │    │ • price    │    ┌─────────────────┐      │      │
│  │  └─────────┘    │ • beds     │    │ property_images │      │      │
│  │                 │ • baths    │    │ property_amenities      │      │
│  │  ┌─────────────┐│ • owner_id │    └─────────────────┘      │      │
│  │  │refresh_     ││            │                              │      │
│  │  │tokens       │└────────────┘                              │      │
│  │  │             │                                             │      │
│  │  │ • token     │                                             │      │
│  │  │ • user_id   │                                             │      │
│  │  │ • expiry    │                                             │      │
│  │  └─────────────┘                                             │      │
│  └──────────────────────────────────────────────────────────────┘      │
└─────────────────────────────────────────────────────────────────────────┘


═══════════════════════════════════════════════════════════════════════════
                            DOCKER ARCHITECTURE
═══════════════════════════════════════════════════════════════════════════

┌─────────────────────────────────────────────────────────────────────────┐
│                         Docker Host Machine                              │
│                                                                          │
│  ┌──────────────────────────────────────────────────────────────┐      │
│  │              Docker Compose (Orchestration)                   │      │
│  └──────────────────────────────────────────────────────────────┘      │
│                                                                          │
│  ┌─────────────────────┬──────────────────┬──────────────────────┐    │
│  │  Container 1        │  Container 2     │  Container 3         │    │
│  │  ┌──────────────┐   │  ┌────────────┐  │  ┌────────────────┐ │    │
│  │  │  Frontend    │   │  │  Backend   │  │  │   PostgreSQL   │ │    │
│  │  │  (React+Nginx│   │  │(Spring Boot│  │  │   Database     │ │    │
│  │  │              │   │  │            │  │  │                │ │    │
│  │  │  Port: 3000  │   │  │ Port: 8001 │  │  │  Port: 5432    │ │    │
│  │  └──────────────┘   │  └────────────┘  │  └────────────────┘ │    │
│  │        │            │        │         │         │            │    │
│  └────────┼────────────┴────────┼─────────┴─────────┼────────────┘    │
│           │                     │                   │                   │
│  ┌────────┼─────────────────────┼───────────────────┼────────────┐    │
│  │        │    rentpeeasy-network (Bridge)          │            │    │
│  │        │                     │                   │            │    │
│  │   nginx:alpine       eclipse-temurin:17   postgres:16-alpine  │    │
│  └──────────────────────────────────────────────────────────────┘    │
│                                                                          │
│  ┌──────────────────────────────────────────────────────────────┐      │
│  │                    Volume: postgres_data                      │      │
│  │              (Persistent database storage)                    │      │
│  └──────────────────────────────────────────────────────────────┘      │
└─────────────────────────────────────────────────────────────────────────┘


═══════════════════════════════════════════════════════════════════════════
                        AUTHENTICATION FLOW
═══════════════════════════════════════════════════════════════════════════

┌─────────┐                                              ┌──────────┐
│ Client  │                                              │  Backend │
└────┬────┘                                              └────┬─────┘
     │                                                        │
     │ 1. POST /api/auth/register                            │
     │    { username, email, password }                      │
     ├──────────────────────────────────────────────────────▶│
     │                                                        │
     │                                    2. Hash password    │
     │                                       (BCrypt)         │
     │                                    3. Save to DB       │
     │                                    4. Generate JWT     │
     │                                    5. Create refresh   │
     │                                       token            │
     │                                                        │
     │ 6. Return tokens + user info                          │
     │◀──────────────────────────────────────────────────────┤
     │    { accessToken, refreshToken, user }                │
     │                                                        │
     │                                                        │
     │ 7. POST /api/auth/login                               │
     │    { username, password }                             │
     ├──────────────────────────────────────────────────────▶│
     │                                                        │
     │                                    8. Verify password  │
     │                                    9. Generate tokens  │
     │                                                        │
     │ 10. Return tokens                                     │
     │◀──────────────────────────────────────────────────────┤
     │                                                        │
     │                                                        │
     │ 11. Authenticated Request                             │
     │     Authorization: Bearer <accessToken>               │
     ├──────────────────────────────────────────────────────▶│
     │                                                        │
     │                                    12. Validate JWT    │
     │                                    13. Extract user    │
     │                                    14. Check perms     │
     │                                    15. Process request │
     │                                                        │
     │ 16. Return response                                   │
     │◀──────────────────────────────────────────────────────┤
     │                                                        │


═══════════════════════════════════════════════════════════════════════════
                        PROPERTY SEARCH FLOW
═══════════════════════════════════════════════════════════════════════════

┌─────────┐                                              ┌──────────┐
│ Client  │                                              │  Backend │
└────┬────┘                                              └────┬─────┘
     │                                                        │
     │ GET /api/properties?city=Bangalore&type=APARTMENT     │
     │                    &minPrice=20000&maxPrice=30000     │
     ├──────────────────────────────────────────────────────▶│
     │                                                        │
     │                                    PropertyController  │
     │                                           ▼            │
     │                                    PropertyService     │
     │                                           ▼            │
     │                                    PropertyRepository  │
     │                                           ▼            │
     │                                    JPA Query:          │
     │                                    SELECT p FROM       │
     │                                    Property p WHERE    │
     │                                    city LIKE '%B%'     │
     │                                    AND type = 'APT'    │
     │                                    AND price >= 20000  │
     │                                    AND price <= 30000  │
     │                                           ▼            │
     │                                    PostgreSQL Query    │
     │                                           ▼            │
     │                                    Map to DTOs         │
     │                                                        │
     │ Return List<PropertyDto>                              │
     │◀──────────────────────────────────────────────────────┤
     │                                                        │


═══════════════════════════════════════════════════════════════════════════
                        DEPLOYMENT ARCHITECTURE
═══════════════════════════════════════════════════════════════════════════

                        ┌────────────────┐
                        │   Load         │
                        │   Balancer     │
                        └────────┬───────┘
                                 │
                 ┌───────────────┼───────────────┐
                 │               │               │
         ┌───────▼──────┐ ┌─────▼──────┐ ┌─────▼──────┐
         │   Frontend   │ │  Frontend  │ │  Frontend  │
         │  Container   │ │ Container  │ │ Container  │
         │  (Nginx)     │ │  (Nginx)   │ │  (Nginx)   │
         └───────┬──────┘ └─────┬──────┘ └─────┬──────┘
                 │              │              │
                 └──────────────┼──────────────┘
                                │
                        ┌───────▼────────┐
                        │   API Gateway  │
                        └───────┬────────┘
                                │
                 ┌──────────────┼──────────────┐
                 │              │              │
         ┌───────▼──────┐ ┌────▼──────┐ ┌────▼──────┐
         │   Backend    │ │  Backend  │ │  Backend  │
         │   Instance   │ │ Instance  │ │ Instance  │
         │ (Spring Boot)│ │(SpringBoot)│(SpringBoot)│
         └───────┬──────┘ └────┬──────┘ └────┬──────┘
                 │              │              │
                 └──────────────┼──────────────┘
                                │
                        ┌───────▼────────┐
                        │   PostgreSQL   │
                        │   Cluster      │
                        │  (Primary +    │
                        │   Replicas)    │
                        └────────────────┘


═══════════════════════════════════════════════════════════════════════════
                        KEY COMPONENTS
═══════════════════════════════════════════════════════════════════════════

1. Security Layer
   • JwtUtil: Token generation and validation
   • JwtAuthenticationFilter: Request interception
   • SecurityConfig: Spring Security configuration
   • BCryptPasswordEncoder: Password hashing

2. Controller Layer
   • AuthController: Authentication endpoints
   • PropertyController: Property management
   • FavoriteController: Favorites management

3. Service Layer
   • AuthService: Business logic for auth
   • PropertyService: Property operations
   • FavoriteService: Favorites operations
   • CustomUserDetailsService: User loading

4. Repository Layer
   • JPA Repositories with custom queries
   • Spring Data JPA integration
   • Query methods and JPQL

5. Entity Layer
   • User: User accounts
   • Property: Property listings
   • Favorite: User favorites
   • RefreshToken: Token management

6. DTO Layer
   • Request DTOs: Input validation
   • Response DTOs: API responses
   • Separation of concerns

7. Exception Handling
   • GlobalExceptionHandler
   • Custom exceptions
   • Error responses


═══════════════════════════════════════════════════════════════════════════

Built with ❤️ using Java Spring Boot, PostgreSQL, React, and Docker
