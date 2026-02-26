# 🗄️ How to Connect to PostgreSQL Database

## 📊 Database Connection Details

When Docker is running (`docker-compose up`):

```
Host:     localhost
Port:     5432
Database: rentpeeasy
Username: postgres
Password: postgres123
```

---

## 🔌 Method 1: Command Line (psql)

### From Your Windows Machine

```powershell
# Connect to database
psql -h localhost -p 5432 -U postgres -d rentpeeasy

# It will ask for password: postgres123

# Once connected:
rentpeeasy=#
```

### Useful psql Commands

```sql
-- List all tables
\dt

-- Describe a table
\d users
\d properties

-- View table data
SELECT * FROM users;
SELECT * FROM properties;

-- Check Liquibase migration history
SELECT * FROM databasechangelog;

-- Exit
\q
```

---

## 🐳 Method 2: Using Docker Exec (Easiest!)

### Connect to PostgreSQL inside Docker container

```powershell
# Connect directly
docker-compose exec postgres psql -U postgres -d rentpeeasy
```

**No password needed!** You're connecting from inside the container.

### Example Session

```powershell
PS> docker-compose exec postgres psql -U postgres -d rentpeeasy

rentpeeasy=# \dt
              List of relations
 Schema |         Name          | Type  |  Owner
--------+-----------------------+-------+----------
 public | databasechangelog     | table | postgres
 public | databasechangeloglock | table | postgres
 public | favorites             | table | postgres
 public | properties            | table | postgres
 public | property_amenities    | table | postgres
 public | property_images       | table | postgres
 public | refresh_tokens        | table | postgres
 public | users                 | table | postgres

rentpeeasy=# SELECT COUNT(*) FROM users;
 count
-------
     0

rentpeeasy=# \q
```

---

## 🖥️ Method 3: GUI Tools

### Option A: pgAdmin 4 (Official PostgreSQL GUI)

**Download:** https://www.pgadmin.org/download/

**Setup:**
1. Install pgAdmin 4
2. Right-click "Servers" → Create → Server
3. Enter details:
   ```
   General Tab:
     Name: RentPEasy Local
   
   Connection Tab:
     Host: localhost
     Port: 5432
     Database: rentpeeasy
     Username: postgres
     Password: postgres123
   ```
4. Click "Save"
5. Browse tables in: Servers → RentPEasy Local → Databases → rentpeeasy → Schemas → public → Tables

---

### Option B: DBeaver (Free, Multi-Database)

**Download:** https://dbeaver.io/download/

**Setup:**
1. Install DBeaver
2. Click "New Database Connection"
3. Select "PostgreSQL"
4. Enter details:
   ```
   Host: localhost
   Port: 5432
   Database: rentpeeasy
   Username: postgres
   Password: postgres123
   ```
5. Click "Test Connection"
6. Click "Finish"

**Pros:**
- ✅ Free & Open Source
- ✅ Supports multiple databases
- ✅ Great query editor
- ✅ ER diagrams
- ✅ Data export/import

---

### Option C: DataGrip (JetBrains - Paid)

**Download:** https://www.jetbrains.com/datagrip/

**Setup:**
1. Install DataGrip
2. Click "+" → Data Source → PostgreSQL
3. Enter connection details
4. Download drivers if prompted
5. Test and Save

**Pros:**
- ✅ Best-in-class query editor
- ✅ Intelligent code completion
- ✅ Database refactoring tools

---

### Option D: IntelliJ IDEA (If you have it)

**Built-in Database Tool:**

1. Open IntelliJ IDEA
2. View → Tool Windows → Database
3. Click "+" → Data Source → PostgreSQL
4. Enter connection details:
   ```
   Host: localhost
   Port: 5432
   Database: rentpeeasy
   User: postgres
   Password: postgres123
   ```
5. Click "Test Connection"
6. Click "OK"

---

## 🔍 Method 4: Using Database URL

If a tool asks for connection URL:

```
jdbc:postgresql://localhost:5432/rentpeeasy?user=postgres&password=postgres123
```

Or separate:
```
URL:      jdbc:postgresql://localhost:5432/rentpeeasy
Username: postgres
Password: postgres123
```

---

## 📊 Verify Database Setup

### Check Tables Created by Liquibase

```powershell
docker-compose exec postgres psql -U postgres -d rentpeeasy -c "\dt"
```

**Expected Output:**
```
              List of relations
 Schema |         Name          | Type  |  Owner
--------+-----------------------+-------+----------
 public | databasechangelog     | table | postgres
 public | databasechangeloglock | table | postgres
 public | favorites             | table | postgres
 public | properties            | table | postgres
 public | property_amenities    | table | postgres
 public | property_images       | table | postgres
 public | refresh_tokens        | table | postgres
 public | users                 | table | postgres
```

### Check Liquibase Migrations

```sql
SELECT id, author, filename, dateexecuted, orderexecuted 
FROM databasechangelog 
ORDER BY orderexecuted;
```

**Expected Output:**
```
              id               |   author   |                  filename                   | dateexecuted | orderexecuted
-------------------------------+------------+--------------------------------------------+--------------+---------------
 001-create-users-table        | rentpeeasy | db/changelog/changes/001-create-users...   | 2026-02-26   | 1
 002-create-properties-table   | rentpeeasy | db/changelog/changes/002-create-prope...   | 2026-02-26   | 2
 003-create-favorites-table    | rentpeeasy | db/changelog/changes/003-create-favor...   | 2026-02-26   | 3
 004-create-refresh-tokens...  | rentpeeasy | db/changelog/changes/004-create-refre...   | 2026-02-26   | 4
 005-create-indexes            | rentpeeasy | db/changelog/changes/005-create-index...   | 2026-02-26   | 5
```

---

## 🧪 Test Data Queries

### Insert a test user

```sql
INSERT INTO users (id, username, email, password, full_name, role, enabled)
VALUES (
  gen_random_uuid(),
  'testuser',
  'test@example.com',
  '$2a$10$abcdefghijklmnopqrstuvwxyz',  -- Hashed password
  'Test User',
  'USER',
  true
);
```

### Query users

```sql
SELECT id, username, email, full_name, role, created_at 
FROM users;
```

### Insert a test property

```sql
INSERT INTO properties (
  id, title, description, type, city, locality, 
  price, owner_id, status
)
VALUES (
  gen_random_uuid(),
  'Beautiful 2BHK Apartment',
  'Spacious apartment in prime location',
  'APARTMENT',
  'Bangalore',
  'Koramangala',
  25000,
  (SELECT id FROM users LIMIT 1),
  'AVAILABLE'
);
```

### Query properties

```sql
SELECT id, title, type, city, price, status 
FROM properties;
```

---

## 🔧 Troubleshooting

### Can't connect from Windows

**Problem:** Connection refused

**Solution 1:** Check Docker is running
```powershell
docker-compose ps
# Should show postgres as "Up"
```

**Solution 2:** Check port 5432 is exposed
```powershell
docker-compose port postgres 5432
# Should show: 0.0.0.0:5432
```

**Solution 3:** Restart Docker
```powershell
docker-compose restart postgres
```

---

### psql command not found

**Solution:** Install PostgreSQL client on Windows

```powershell
# Using Chocolatey
choco install postgresql

# Or download from: https://www.postgresql.org/download/windows/
```

---

### Password authentication failed

**Check credentials:**
- Username: `postgres` (not POSTGRES)
- Password: `postgres123`
- Database: `rentpeeasy`

**Verify in docker-compose.yml:**
```yaml
environment:
  POSTGRES_DB: rentpeeasy
  POSTGRES_USER: postgres
  POSTGRES_PASSWORD: postgres123
```

---

### Database is empty

**Check Liquibase ran:**
```sql
SELECT * FROM databasechangelog;
```

**If empty, check backend logs:**
```powershell
docker-compose logs backend | findstr Liquibase
```

**Restart backend to trigger migrations:**
```powershell
docker-compose restart backend
```

---

## 📚 Quick Reference

### Connection String Formats

**JDBC:**
```
jdbc:postgresql://localhost:5432/rentpeeasy
```

**psql:**
```
postgresql://postgres:postgres123@localhost:5432/rentpeeasy
```

**URL format:**
```
postgres://postgres:postgres123@localhost:5432/rentpeeasy
```

---

### Common Commands Cheat Sheet

```powershell
# Connect via Docker
docker-compose exec postgres psql -U postgres -d rentpeeasy

# Connect via psql (from Windows)
psql -h localhost -p 5432 -U postgres -d rentpeeasy

# List databases
psql -h localhost -U postgres -l

# Backup database
docker-compose exec postgres pg_dump -U postgres rentpeeasy > backup.sql

# Restore database
docker-compose exec -T postgres psql -U postgres rentpeeasy < backup.sql

# View logs
docker-compose logs postgres
```

---

## 🎯 Recommended Tools

| Tool | Best For | Cost |
|------|----------|------|
| **Docker Exec** | Quick queries | Free ✅ |
| **DBeaver** | Regular development | Free ✅ |
| **pgAdmin 4** | PostgreSQL specific | Free ✅ |
| **IntelliJ IDEA** | If already using | Paid 💰 |
| **DataGrip** | Professional work | Paid 💰 |

---

## ✅ Summary

**Easiest method:**
```powershell
docker-compose exec postgres psql -U postgres -d rentpeeasy
```

**For GUI:**
1. Download DBeaver
2. Connect with: localhost:5432
3. Username: postgres, Password: postgres123

**Connection works when:**
- ✅ Docker is running: `docker-compose ps`
- ✅ Port 5432 is exposed
- ✅ Backend has started (triggers Liquibase)

---

**Now you can connect to your database and see all the tables! 🎉**
