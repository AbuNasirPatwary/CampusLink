# Final Implementation Notes

## Requirement priority used

1. Latest user decisions made during the final design pass.
2. Final Figma Make visual/workflow direction.
3. Existing full-stack code, reused and corrected rather than regenerated unnecessarily.
4. Older handoff document only for requirements that were not superseded.

## Final access model

- STUDENT: Student ID + UIU email → ACTIVE → full normal-user workspace.
- ALUMNI: previous Student ID + any valid email → PENDING → Admin approval → same full normal-user workspace.
- ADMIN: separate operational workspace.

## Important FindIt privacy implementation

- LOST image: stored in public `uploads/findit` and returned in normal DTOs.
- FOUND verification image: stored in `private-uploads/findit`.
- Normal `FindItPostResponse` deliberately returns `imageUrl = null` for FOUND posts.
- Admin claim response contains an internal private key only.
- Private image bytes are fetched through `/api/media/private/...`, protected with `hasRole('ADMIN')`.

## Threading requirement

Preserved and expanded. Important actions call `AsyncActivityLogService.record(...)`, which runs using the `campusTaskExecutor` pool. Worker names begin with `campus-worker-` and the Admin Dashboard displays activity logs for the viva/demo.

## Local run

Backend requirements: Java 21, Maven 3.9+, PostgreSQL 15+.

Frontend requirements: Node.js 20+.

Demo admin: `admin@campuslink.edu / Admin@123`.

Default UIU email suffix: `@uiu.ac.bd` (override with `UNIVERSITY_EMAIL_DOMAIN`).
