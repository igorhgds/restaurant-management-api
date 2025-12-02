CREATE TABLE IF NOT EXISTS dishes(
    dish_id     UUID                    PRIMARY KEY,
    name        VARCHAR(150)            UNIQUE  NOT NULL,
    price       DECIMAL(10, 2)          NOT NULL,
    description TEXT,
    created_at  TIMESTAMP               NOT NULL,
    updated_at  TIMESTAMP
);