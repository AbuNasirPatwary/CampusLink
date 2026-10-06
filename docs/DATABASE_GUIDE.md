# CampusLink Database Guide

## How PostgreSQL is connected

Spring Boot reads these settings from `backend/src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/campuslink
spring.datasource.username=postgres
spring.datasource.password=postgres
spring.jpa.hibernate.ddl-auto=update
```

The Java `@Entity` classes define the relational structure. Spring Data JPA/Hibernate translates repository calls into SQL automatically.

So when faculty asks **“Where is the SQL?”**, the simple answer is:

> We use Spring Data JPA. The entity classes describe the tables, repository interfaces describe data access, and Hibernate generates the SQL automatically. PostgreSQL is still the actual database.

For example:

```java
@Entity
@Table(name = "borrow_requests")
public class BorrowRequest { ... }
```

maps to PostgreSQL table:

```text
borrow_requests
```

and:

```java
requestRepository.save(request)
```

causes Hibernate to execute an SQL `INSERT` or `UPDATE`.

---

## Feature → main tables

| Feature | PostgreSQL tables |
|---|---|
| Accounts | `app_users` |
| FindIt | `findit_posts`, `findit_claims` |
| BorrowHub | `borrow_requests`, `borrow_comments` |
| Complaints | `complaint_posts`, `complaint_comments`, `complaint_reactions` |
| TalentHub | `talent_posts`, `talent_comments`, `talent_reactions`, `talent_bookmarks` |
| Moderation | `content_reports` |
| Notifications | `notifications` |
| Thread demo / audit | `activity_logs` |

---

## Important relationships

- One user can create many posts.
- One FindIt post can receive many claims.
- One BorrowHub request can receive many comments.
- One complaint can receive many comments and reactions.
- One TalentHub post can receive many comments, reactions and bookmarks.
- `@ManyToOne` is used when many records belong to one parent/user.

Example:

```text
app_users 1 ──── * borrow_requests
borrow_requests 1 ──── * borrow_comments
```
