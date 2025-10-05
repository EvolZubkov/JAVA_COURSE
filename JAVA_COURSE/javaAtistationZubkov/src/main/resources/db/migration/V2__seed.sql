-- Сидинг справочника статусов
INSERT INTO order_status(name) VALUES
 ('NEW'), ('PAID'), ('SHIPPED'), ('DELIVERED'), ('CANCELED'), ('RETURNED'), ('REFUNDED'), ('PROCESSING'), ('ON_HOLD'), ('FAILED')
ON CONFLICT DO NOTHING;

-- Товары (10+)
INSERT INTO product(description, price, quantity, category) VALUES
 ('Keyboard', 49.90, 100, 'Peripherals'),
 ('Mouse', 19.90, 200, 'Peripherals'),
 ('Monitor 24"', 159.00, 50, 'Displays'),
 ('Headphones', 79.90, 120, 'Audio'),
 ('Webcam', 39.90, 80, 'Video'),
 ('USB-C Hub', 29.90, 150, 'Accessories'),
 ('Laptop Stand', 34.50, 60, 'Accessories'),
 ('Desk Lamp', 22.00, 70, 'Office'),
 ('Microphone', 89.00, 40, 'Audio'),
 ('Chair', 129.00, 35, 'Office')
ON CONFLICT DO NOTHING;

-- Покупатели (10+)
INSERT INTO customer(first_name, last_name, phone, email) VALUES
 ('Alice',  'Smith',    '+1-202-000-0001', 'alice@example.com'),
 ('Bob',    'Johnson',  '+1-202-000-0002', 'bob@example.com'),
 ('Carol',  'Davis',    '+1-202-000-0003', 'carol@example.com'),
 ('Dan',    'Miller',   '+1-202-000-0004', 'dan@example.com'),
 ('Eve',    'Wilson',   '+1-202-000-0005', 'eve@example.com'),
 ('Frank',  'Moore',    '+1-202-000-0006', 'frank@example.com'),
 ('Grace',  'Taylor',   '+1-202-000-0007', 'grace@example.com'),
 ('Heidi',  'Anderson', '+1-202-000-0008', 'heidi@example.com'),
 ('Ivan',   'Thomas',   '+1-202-000-0009', 'ivan@example.com'),
 ('Judy',   'Jackson',  '+1-202-000-0010', 'judy@example.com')
ON CONFLICT DO NOTHING;

-- Заказы (10+), статус: NEW (1) / PAID (2) / SHIPPED (3) и т.д.
-- Колонки: id (SERIAL), product_id, customer_id, order_date, quantity, status_id
INSERT INTO orders(product_id, customer_id, order_date, quantity, status_id) VALUES
 (1, 1, CURRENT_DATE - INTERVAL '9 day', 1, 2),
 (2, 2, CURRENT_DATE - INTERVAL '8 day', 2, 3),
 (3, 3, CURRENT_DATE - INTERVAL '7 day', 1, 1),
 (4, 4, CURRENT_DATE - INTERVAL '6 day', 3, 2),
 (5, 5, CURRENT_DATE - INTERVAL '5 day', 1, 3),
 (6, 6, CURRENT_DATE - INTERVAL '4 day', 2, 2),
 (7, 7, CURRENT_DATE - INTERVAL '3 day', 1, 1),
 (8, 8, CURRENT_DATE - INTERVAL '2 day', 1, 4),
 (9, 9, CURRENT_DATE - INTERVAL '1 day', 2, 2),
 (10, 10, CURRENT_DATE,                    1, 1)
ON CONFLICT DO NOTHING;
