CREATE TABLE IF NOT EXISTS menu(
    menu_id                     UUID            PRIMARY KEY,
    name                        VARCHAR(50)     NOT NULL,
    description                 TEXT,
    active                      BOOLEAN         NOT NULL,
    start_date                  TIMESTAMP,
    end_date                    TIMESTAMP,
    created_at                  TIMESTAMP       NOT NULL,
    updated_at                  TIMESTAMP
);

CREATE TABLE IF NOT EXISTS menu_dishes(
    menu_id                     UUID            NOT NULL,
    dish_id                     UUID            NOT NULL,
    PRIMARY KEY (menu_id, dish_id),
    CONSTRAINT fk_menu_dishes_menu FOREIGN KEY (menu_id) REFERENCES menu(menu_id) ON DELETE CASCADE,
    CONSTRAINT fk_menu_dishes_dish FOREIGN KEY (dish_id) REFERENCES dishes(dish_id) ON DELETE CASCADE
);