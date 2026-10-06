# Faculty Demo Checklist — Final Phase One

## Before class

1. Start PostgreSQL and create database `campuslink`.
2. Start Spring Boot backend on port 8080.
3. Start React frontend on port 5173.
4. Keep demo admin ready: `admin@campuslink.edu / Admin@123`.
5. Register at least one Student account and one Alumni account.
6. Login as Admin and approve the Alumni account.
7. Create sample content in each module.

## Recommended demonstration order

### 1. Architecture
Explain:

`React + Vite → REST/JSON → Spring Boot → Spring Data JPA/Hibernate → PostgreSQL`

### 2. Registration, login and roles
- Register Student using Student ID + UIU email; show ACTIVE account.
- Register Alumni using previous Student ID + personal email; show PENDING state.
- Admin → User Verification → approve Alumni.
- Login as approved Alumni and show that Student and Alumni now use the same full workspace.
- Login as Admin and show the separate admin workspace.

### 3. FindIt privacy + claim flow
- Create LOST post; optional photo is public.
- Create FOUND post; verification photo is required.
- Login as another normal user: show FOUND description but no photo.
- Submit ownership details.
- Admin → FindIt Claims: show the private verification photo only here.
- Approve claim, perform physical-handover explanation, then Mark Delivered.

### 4. BorrowHub
- Create “Need an ESP32” request.
- Another user replies publicly.
- Explain that further coordination uses the registered email outside CampusLink.
- Requester marks Completed.

### 5. Complaint Board
- Create a complaint with optional media.
- React/comment.
- Admin changes OPEN → FORWARDED/RESOLVED and adds Official Response.

### 6. TalentHub
- Create Writing and Photography/Art/Video examples.
- Like, comment and bookmark.
- Report a post.
- Admin reviews report and can feature/remove content.

### 7. Threading — MUST SHOW
- Login or create a BorrowHub/TalentHub/FindIt/Complaint action.
- Admin Dashboard → Threaded Activity Logs.
- Point to a `campus-worker-*` worker name.
- Open `AsyncConfig.java` and `AsyncActivityLogService.java`.

Explain: the main request performs the business action, while secondary audit logging runs asynchronously on a reusable Java thread pool.

### 8. Database
Show tables such as `app_users`, `findit_posts`, `findit_claims`, `borrow_requests`, `complaint_posts`, `talent_posts`, `notifications`, `content_reports`, and `activity_logs`, then open the matching Entity/Repository files.
