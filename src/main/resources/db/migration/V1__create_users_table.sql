CREATE TABLE users (
                       id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                       email           VARCHAR(255) NOT NULL UNIQUE,
                       password_hash   VARCHAR(255) NOT NULL,
                       sites_published INT NOT NULL DEFAULT 0,
                       sites_limit     INT NOT NULL DEFAULT 3,
                       is_admin        BOOLEAN NOT NULL DEFAULT false,
                       created_at      TIMESTAMP NOT NULL DEFAULT NOW(),
                       updated_at      TIMESTAMP NOT NULL DEFAULT NOW(),
                       deleted_at      TIMESTAMP
);

CREATE INDEX idx_users_email ON users(email);