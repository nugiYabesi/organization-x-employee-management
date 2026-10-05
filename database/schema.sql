-- Run this once in pgAdmin/psql as a PostgreSQL administrator.
CREATE DATABASE organization_x;

-- After connecting to organization_x, the Java application will create
-- the employees table automatically. The equivalent table definition is:
--
-- CREATE TABLE employees (
--     id INTEGER PRIMARY KEY,
--     name VARCHAR(100) NOT NULL,
--     department VARCHAR(100) NOT NULL,
--     hourly_rate NUMERIC(12,2) NOT NULL CHECK (hourly_rate >= 0),
--     hours_worked NUMERIC(12,2) NOT NULL CHECK (hours_worked >= 0),
--     created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
-- );
