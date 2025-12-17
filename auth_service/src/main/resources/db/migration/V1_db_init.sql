CREATE TABLE oauth2_authorized_client (
    id BIGINT NOT NULL,
    client_registration_id VARCHAR(100) NOT NULL,
    principal_name VARCHAR(200) NOT NULL,
    access_token_type VARCHAR(100),
    access_token_value TEXT,
    access_token_issued_at TIMESTAMP,
    access_token_expires_at TIMESTAMP,
    access_token_scopes TEXT,
    refresh_token_value TEXT,
    refresh_token_issued_at TIMESTAMP,
    CONSTRAINT oauth2_authorized_client_pkey PRIMARY KEY (id)
);