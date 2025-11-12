CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE IF NOT EXISTS users(
    user_id                     UUID            PRIMARY KEY,
    username                    VARCHAR(150)    NOT NULL,
    email                       VARCHAR(150)    NOT NULL,
    password                    VARCHAR(20)     NOT NULL,
    password_recovery_code      VARCHAR(4),
    user_role                   VARCHAR(50)     NOT NULL,
    is_enabled                  BOOLEAN         NOT NULL,
    created_at                  TIMESTAMP,
    updated_at                  TIMESTAMP
);