CREATE TYPE role_enum AS ENUM ('ADMIN','FACULTY','STUDENT');

CREATE TABLE roles (
    id SERIAL PRIMARY KEY,
    name role_enum UNIQUE NOT NULL
);

CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    name VARCHAR(255) NOT NULL,
    password VARCHAR(255),
    created_at TIMESTAMPTZ DEFAULT NOW(),
    updated_at TIMESTAMPTZ DEFAULT NOW()
);

CREATE TABLE user_roles (
    user_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
    role_id INT REFERENCES roles(id) ON DELETE CASCADE,
    PRIMARY KEY (user_id, role_id)
);

CREATE TABLE departments (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) UNIQUE NOT NULL
);

CREATE TABLE semesters (
    id SERIAL PRIMARY KEY,
    department_id INT REFERENCES departments(id) ON DELETE CASCADE,
    name VARCHAR(100) NOT NULL
);

CREATE TABLE subjects (
    id SERIAL PRIMARY KEY,
    semester_id INT REFERENCES semesters(id) ON DELETE CASCADE,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE audit_logs (
    id BIGSERIAL PRIMARY KEY,
    actor_id BIGINT REFERENCES users(id),
    action VARCHAR(255) NOT NULL,
    created_at TIMESTAMPTZ DEFAULT NOW()
);


