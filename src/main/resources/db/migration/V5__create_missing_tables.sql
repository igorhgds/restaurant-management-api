CREATE TABLE IF NOT EXISTS restaurant_table(
    table_id                    UUID            PRIMARY KEY,
    number                      INT             NOT NULL,
    capacity                    INT             NOT NULL,
    status                      VARCHAR(20)     NOT NULL,
    location                    VARCHAR(20)     NOT NULL,
    created_at                  TIMESTAMP       NOT NULL,
    updated_at                  TIMESTAMP
);

CREATE TABLE IF NOT EXISTS image(
    image_id                    UUID            PRIMARY KEY,
    uri                         VARCHAR(255)    NOT NULL,
    original_filename           VARCHAR(255)    NOT NULL
);

CREATE TABLE IF NOT EXISTS dish_images(
    dish_id                     UUID            NOT NULL,
    image_id                    UUID            NOT NULL,
    PRIMARY KEY (dish_id, image_id),
    CONSTRAINT fk_dish_images_dish FOREIGN KEY (dish_id) REFERENCES dishes(dish_id) ON DELETE CASCADE,
    CONSTRAINT fk_dish_images_image FOREIGN KEY (image_id) REFERENCES image(image_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS restaurant_order(
    order_id                    UUID            PRIMARY KEY,
    status                      VARCHAR(20)     NOT NULL,
    price                       DECIMAL(10, 2)  NOT NULL,
    table_id                    UUID            NOT NULL,
    waiter_id                   UUID            NOT NULL,
    created_at                  TIMESTAMP       NOT NULL,
    updated_at                  TIMESTAMP,
    CONSTRAINT fk_order_table FOREIGN KEY (table_id) REFERENCES restaurant_table(table_id),
    CONSTRAINT fk_order_waiter FOREIGN KEY (waiter_id) REFERENCES users(user_id)
);

CREATE TABLE IF NOT EXISTS order_item(
    order_item_id               UUID            PRIMARY KEY,
    quantity                    INT             NOT NULL,
    unit_price                  DECIMAL(10, 2)  NOT NULL,
    subtotal                    DECIMAL(10, 2)  NOT NULL,
    order_id                    UUID            NOT NULL,
    dish_id                     UUID            NOT NULL,
    CONSTRAINT fk_order_item_order FOREIGN KEY (order_id) REFERENCES restaurant_order(order_id) ON DELETE CASCADE,
    CONSTRAINT fk_order_item_dish FOREIGN KEY (dish_id) REFERENCES dishes(dish_id)
);
