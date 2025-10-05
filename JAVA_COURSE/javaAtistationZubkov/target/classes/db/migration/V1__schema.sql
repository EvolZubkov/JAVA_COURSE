-- Схема БД для приложения "учёт заказов"
-- Соответствует ТЗ: product, customer, orders, order_status
-- Используем IF NOT EXISTS, PK/FK/NOT NULL/CHECK, индексы и комментарии

CREATE TABLE IF NOT EXISTS order_status (
    id          SERIAL PRIMARY KEY,
    name        TEXT NOT NULL UNIQUE
);

COMMENT ON TABLE order_status IS 'Справочник статусов заказов';
COMMENT ON COLUMN order_status.name IS 'Имя статуса (например: NEW, PAID, SHIPPED, CANCELED)';

CREATE TABLE IF NOT EXISTS product (
    id          SERIAL PRIMARY KEY,
    description TEXT        NOT NULL,
    price       NUMERIC(12,2) NOT NULL CHECK (price >= 0),
    quantity    INTEGER     NOT NULL CHECK (quantity >= 0),
    category    TEXT        NOT NULL
);

COMMENT ON TABLE product IS 'Товары';
COMMENT ON COLUMN product.description IS 'Описание товара';
COMMENT ON COLUMN product.price IS 'Стоимость товара (>= 0)';
COMMENT ON COLUMN product.quantity IS 'Количество на складе (>= 0)';
COMMENT ON COLUMN product.category IS 'Категория товара';

CREATE TABLE IF NOT EXISTS customer (
    id        SERIAL PRIMARY KEY,
    first_name TEXT      NOT NULL,
    last_name  TEXT      NOT NULL,
    phone      TEXT      NOT NULL,
    email      TEXT      NOT NULL UNIQUE
        CHECK (position('@' in email) > 1)
);

COMMENT ON TABLE customer IS 'Покупатели';
COMMENT ON COLUMN customer.first_name IS 'Имя';
COMMENT ON COLUMN customer.last_name  IS 'Фамилия';
COMMENT ON COLUMN customer.phone      IS 'Телефон (произвольная валидация на уровне приложения)';
COMMENT ON COLUMN customer.email      IS 'Email (минимальная проверка на наличие @)';

CREATE TABLE IF NOT EXISTS orders (
    id           SERIAL PRIMARY KEY,
    product_id   INTEGER  NOT NULL,
    customer_id  INTEGER  NOT NULL,
    order_date   DATE     NOT NULL DEFAULT CURRENT_DATE,
    quantity     INTEGER  NOT NULL CHECK (quantity > 0),
    status_id    INTEGER  NOT NULL,

    CONSTRAINT fk_orders_product   FOREIGN KEY (product_id)  REFERENCES product(id)  ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT fk_orders_customer  FOREIGN KEY (customer_id) REFERENCES customer(id) ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT fk_orders_status    FOREIGN KEY (status_id)   REFERENCES order_status(id) ON UPDATE CASCADE ON DELETE RESTRICT
);

COMMENT ON TABLE orders IS 'Заказы';
COMMENT ON COLUMN orders.order_date IS 'Дата заказа';
COMMENT ON COLUMN orders.quantity   IS 'Количество в заказе (> 0)';

-- Индексы по внешним ключам и дате заказа
CREATE INDEX IF NOT EXISTS idx_orders_product_id  ON orders(product_id);
CREATE INDEX IF NOT EXISTS idx_orders_customer_id ON orders(customer_id);
CREATE INDEX IF NOT EXISTS idx_orders_status_id   ON orders(status_id);
CREATE INDEX IF NOT EXISTS idx_orders_order_date  ON orders(order_date);
