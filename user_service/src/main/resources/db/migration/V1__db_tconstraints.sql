ALTER TABLE users
    ADD CONSTRAINT uk_users_user_name UNIQUE (user_name);

ALTER TABLE users
    ADD CONSTRAINT uk_users_email UNIQUE (email);

ALTER TABLE users
    ADD CONSTRAINT chk_users_role
    CHECK (role IN ('ADMIN', 'USER', 'TEACHER', 'STUDENT'));

CREATE UNIQUE INDEX ux_users_person_id
    ON users (person_id)
    WHERE person_id IS NOT NULL;

CREATE INDEX idx_users_email
    ON users (email);

CREATE INDEX idx_users_last_login
    ON users (last_login_date);