# CampusLink Threading Guide

The original AOOP requirement included threading. Because the frontend is now React.js, the explicit Java multithreading demonstration is placed in the **Spring Boot backend**.

## Files

```text
backend/src/main/java/com/campuslink/config/AsyncConfig.java
backend/src/main/java/com/campuslink/audit/AsyncActivityLogService.java
backend/src/main/java/com/campuslink/audit/ActivityLog.java
backend/src/main/java/com/campuslink/audit/AdminActivityLogController.java
```

## How it works

`AsyncConfig` creates a reusable thread pool:

```java
executor.setCorePoolSize(3);
executor.setMaxPoolSize(6);
executor.setThreadNamePrefix("campus-worker-");
```

The audit method is marked:

```java
@Async("campusTaskExecutor")
public void record(...) { ... }
```

When a normal request does something important, such as creating a TalentHub post, it calls:

```java
activityLogs.record(...)
```

Spring schedules that work on a separate worker thread instead of doing the audit insert on the original HTTP request thread.

## Demo idea

1. Login as a student.
2. Create a BorrowHub request or TalentHub post.
3. Login as admin.
4. Open **Admin → Threaded Activity Logs**.
5. Point to a row such as:

```text
Action: BORROWHUB_CREATE
Worker thread: campus-worker-2
```

Faculty explanation:

> The main request completes the business action, while the audit record is processed by a background thread from our thread pool. This is explicit multithreading in the Java backend.

## Why this is useful

Audit logging is secondary work. It should not make the main user operation wait longer than necessary. The thread pool also avoids creating unlimited raw `Thread` objects manually.
