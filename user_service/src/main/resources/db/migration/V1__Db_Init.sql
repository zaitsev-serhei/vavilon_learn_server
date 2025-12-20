CREATE SEQUENCE persons_seq START WITH 1 INCREMENT BY 1;
CREATE TABLE persons (
    id                 BIGSERIAL PRIMARY KEY,
    first_name         VARCHAR(100),
    last_name          VARCHAR(100),
    birth_date         DATE,
    country            VARCHAR(100),
    city               VARCHAR(100),

    created_by         BIGINT,
    created_at         TIMESTAMPTZ NOT NULL DEFAULT now(),
    last_modified_by   BIGINT,
    last_modified_at   TIMESTAMPTZ
);
CREATE SEQUENCE users_seq START WITH 1 INCREMENT BY 1;
CREATE TABLE users (
    id                     BIGSERIAL PRIMARY KEY,
    user_name              VARCHAR(150) NOT NULL,
    password               VARCHAR(255),
    email                  VARCHAR(255) NOT NULL,
    role                   VARCHAR(50) NOT NULL,
    active                 BOOLEAN NOT NULL DEFAULT true,
    locked                 BOOLEAN NOT NULL DEFAULT false,
    credentials_expired    BOOLEAN NOT NULL DEFAULT false,
    last_login_date        TIMESTAMPTZ,

    person_id              BIGINT,

    created_by             BIGINT,
    created_at             TIMESTAMPTZ NOT NULL DEFAULT now(),
    last_modified_by       BIGINT,
    last_modified_at       TIMESTAMPTZ,

    CONSTRAINT fk_users_person
        FOREIGN KEY (person_id)
        REFERENCES persons (id)
        ON DELETE CASCADE
);