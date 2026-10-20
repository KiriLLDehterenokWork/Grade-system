CREATE TABLE study_groups (
                              id BIGSERIAL PRIMARY KEY,
                              name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE subjects (
                          id BIGSERIAL PRIMARY KEY,
                          name VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE users (
                       id BIGSERIAL PRIMARY KEY,
                       username VARCHAR(50) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL,
                       full_name VARCHAR(100) NOT NULL,
                       role VARCHAR(20) NOT NULL,
                       group_id BIGINT REFERENCES study_groups(id) ON DELETE SET NULL
);

CREATE TABLE teacher_subjects (
                                  teacher_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
                                  subject_id BIGINT NOT NULL REFERENCES subjects(id) ON DELETE CASCADE,
                                  PRIMARY KEY (teacher_id, subject_id)
);

CREATE TABLE grades (
                        id BIGSERIAL PRIMARY KEY,
                        value INTEGER NOT NULL CHECK (value >= 1 AND value <= 10),
                        grade_date DATE NOT NULL,
                        student_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
                        subject_id BIGINT NOT NULL REFERENCES subjects(id) ON DELETE CASCADE,
                        teacher_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE
);