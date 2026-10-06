-- Run this once as a PostgreSQL user that can create databases.
CREATE DATABASE campuslink;

-- CampusLink uses Spring Data JPA/Hibernate.
-- After the backend starts, Hibernate creates/updates the Phase-One tables automatically.
-- See docs/DATABASE_GUIDE.md for the table-to-feature mapping.
