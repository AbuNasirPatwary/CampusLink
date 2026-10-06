# CampusLink — Final Phase-One Requirements

This file reflects the **latest project decisions**. Where an older handoff document differs, this file and the implemented code take priority.

## Accounts and access

### Current Student
- Registers with **Student ID + configured UIU university email**.
- Account becomes `ACTIVE` after successful Phase-One registration checks.
- Has full access to Home, FindIt, BorrowHub, Complaints, TalentHub, Notifications and Profile.

### Alumni
- Registers with **previous UIU Student ID + any valid email address**.
- Account starts as `PENDING` and must be manually approved by an administrator.
- After approval (`ACTIVE`), Alumni use the **same dashboard, profile structure and full feature set as Students**.
- There is no separate restricted Alumni application.

### Administrator
- Uses a separate administration workspace.
- Handles Alumni approval/rejection, FindIt ownership claims and delivery, Complaint status/official responses, TalentHub moderation, reports and threaded activity logs.

## FindIt

- Available to ACTIVE Students and ACTIVE Alumni.
- LOST post: description is public; photo is optional and public when provided.
- FOUND post: description is public; a verification photo is required.
- FOUND verification photos are stored in a private backend directory and are **not returned by normal user API responses**.
- A claimant submits identifying ownership details without seeing the private image.
- Admin compares the claim against the private image/item information, then approves or rejects it.
- Physical handover happens offline; admin finally marks the post `DELIVERED`.
- No smart matching, image recognition, QR verification, AI scoring, maps or private chat.

## BorrowHub

- Available to ACTIVE Students and ACTIVE Alumni.
- Users post temporary borrowing requests and other users reply publicly.
- Registered account email may be used for coordination outside CampusLink.
- Request owner marks the request `COMPLETED`.
- No marketplace, payment, deposit, rating, return-tracking or private-chat workflow.

## Complaint Board

- Available to ACTIVE Students and ACTIVE Alumni.
- Posts can contain text and optional image/video media.
- Other users can react and comment.
- Statuses: `OPEN`, `FORWARDED`, `RESOLVED`, `REMOVED`.
- Admin may publish an official response.
- No automated priority scoring, maps or departmental auto-routing.

## TalentHub

Categories:
- Writing
- Video
- Photography
- Art & Design

ACTIVE Students and Alumni can create, edit/delete their own work, like/react, comment, bookmark and report content. Admin can review reports, feature work and remove inappropriate posts.

No followers, private DMs, live streaming, monetization or AI recommendations.

## Threading / AOOP requirement

The Java backend must keep the explicit multithreading demonstration:

`AsyncConfig` → `ThreadPoolTaskExecutor` → `AsyncActivityLogService.record(...)` with `@Async("campusTaskExecutor")` → `activity_logs`.

Worker thread names use `campus-worker-*` and are visible in the Admin Dashboard activity-log panel for the faculty demonstration.
