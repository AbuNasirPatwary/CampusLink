# CampusLink — Phase-One Full-Stack Project

**Stack:** React.js + Vite + Tailwind CSS → REST/JSON → Spring Boot → PostgreSQL

CampusLink is a university-only community platform. Phase One contains:

1. User Account & Profile Management
2. FindIt — Lost & Found (active students + approved alumni)
3. BorrowHub — Borrowing Request Board (active students + approved alumni)
4. Campus Complaint Board (active students + approved alumni)
5. TalentHub (active students + approved alumni)
6. Shared notifications, content reporting, admin moderation
7. Explicit background threading in Spring Boot for asynchronous activity logging

The source code is intentionally separated by feature so it is easy to show and explain to faculty.

---

## 1. Project folders

```text
CampusLink/
├── frontend/                 React.js user interface
├── backend/                  Spring Boot REST API
├── database/                 PostgreSQL setup helper
├── docs/                     Faculty-friendly explanations
└── docker-compose.yml        Optional PostgreSQL startup
```

### Backend feature packages

```text
com.campuslink
├── auth/          registration + login
├── user/          profiles + role/account management
├── findit/        lost/found posts + claims
├── borrowhub/     borrowing requests + comments
├── complaint/     complaint posts + reactions + comments
├── talent/        TalentHub posts + likes + comments + bookmarks
├── report/        report inappropriate content
├── notification/  notifications
├── media/         local file upload
├── audit/         threaded activity logging
├── security/      JWT authentication
├── config/        security, CORS, threading, seed admin
└── common/        shared base classes + exception handling
```

---

## 2. Requirements

Install:

- Java 21
- Maven 3.9+
- Node.js 20+
- PostgreSQL 15+ (or Docker)

---

## 3. Start PostgreSQL

### Option A — PostgreSQL already installed

Open pgAdmin or `psql`, then run:

```sql
CREATE DATABASE campuslink;
```

Default backend database settings:

```text
Database: campuslink
Username: postgres
Password: postgres
Port: 5432
```

If your PostgreSQL password is different, set `DB_PASSWORD` before starting Spring Boot.

### Option B — Docker

From the root folder:

```bash
docker compose up -d postgres
```

---

## 4. Start Spring Boot backend

Open a terminal in `backend/`.

### Windows PowerShell example

```powershell
$env:DB_USERNAME="postgres"
$env:DB_PASSWORD="YOUR_POSTGRES_PASSWORD"
$env:UNIVERSITY_EMAIL_DOMAIN="uiu.ac.bd"
mvn spring-boot:run
```

Backend runs at:

```text
http://localhost:8080
```

### Demo admin

The backend creates one administrator automatically on first startup:

```text
Email: admin@campuslink.edu
Password: Admin@123
```

Change these with environment variables before a real deployment:

```text
ADMIN_EMAIL
ADMIN_PASSWORD
JWT_SECRET
```

---

## 5. Start React frontend

Open another terminal in `frontend/`:

```bash
npm install
npm run dev
```

Frontend runs at:

```text
http://localhost:5173
```

---

## 6. Student email rule

Current students must register with a configured university email domain.

Default:

```text
@uiu.ac.bd
```

Set your actual university domain before the backend starts:

```powershell
$env:UNIVERSITY_EMAIL_DOMAIN="uiu.ac.bd"
```

Students register with Student ID + the configured UIU email domain. Alumni register with their previous Student ID + any valid email; their account starts as `PENDING` until an admin approves it. Once ACTIVE, Student and Alumni accounts have the same full CampusLink access.

---

## 7. File uploads

Files are stored locally under:

```text
backend/uploads/                 public uploads
├── profiles/
├── findit/                     LOST-item public photos
├── borrowhub/
├── complaints/
└── talent/

backend/private-uploads/findit/  FOUND-item admin-only verification photos
```

PostgreSQL stores only file paths/keys, not binary media. FOUND-item verification images are stored separately under `private-uploads` and are served only through an ADMIN-authorized API endpoint; normal FindIt responses omit those image paths entirely.

---

## 8. Threading requirement

CampusLink contains an explicit Spring Boot thread pool:

```text
AsyncConfig.java
    ↓
ThreadPoolTaskExecutor
    ↓
AsyncActivityLogService.record(...)
    ↓
activity_logs table
```

Registration, login and important module actions submit audit work to a background thread named like:

```text
campus-worker-1
campus-worker-2
```

The administrator can open **Threaded Activity Logs** in the Admin Panel and show the worker-thread name during the faculty demo.

See `docs/THREADING_GUIDE.md`.

---

## 9. Very important faculty explanation path

Example — **Login button**:

```text
LoginPage.jsx
   ↓
authService.js
   ↓
POST /api/auth/login
   ↓
AuthController.java
   ↓
AuthService.java
   ↓
UserRepository.java
   ↓
PostgreSQL: app_users
```

Example — **Create BorrowHub request**:

```text
CreateBorrowRequestPage.jsx
   ↓
borrowHubService.js
   ↓
BorrowHubController.java
   ↓
BorrowHubService.java
   ↓
BorrowRequestRepository.java
   ↓
PostgreSQL: borrow_requests
```

For every module, see `docs/FACULTY_CODE_MAP.md`.

---

## 10. Phase-One scope intentionally NOT included

The code does not reintroduce features you removed during planning, such as:

- FindIt automatic item matching
- FindIt image recognition / QR verification
- BorrowHub rental marketplace workflow
- BorrowHub payment/deposit/return tracking
- Private chat
- Complaint categories or automatic department routing
- TalentHub followers, live streaming, monetization or AI recommendations

These remain future-phase ideas.
