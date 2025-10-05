-- ===== ЧТЕНИЕ (5+ запросов) =====

-- 1) Список всех заказов за последние 7 дней с именем покупателя и описанием товара
SELECT o.id AS order_id, o.order_date, c.first_name || ' ' || c.last_name AS customer,
       p.description AS product, o.quantity, os.name AS status
FROM orders o
JOIN customer c ON c.id = o.customer_id
JOIN product  p ON p.id = o.product_id
JOIN order_status os ON os.id = o.status_id
WHERE o.order_date >= CURRENT_DATE - INTERVAL '7 day'
ORDER BY o.order_date DESC, o.id DESC;

-- 2) Топ-3 самых популярных товаров (по суммарному количеству в заказах)
SELECT p.description, SUM(o.quantity) AS total_qty
FROM orders o
JOIN product p ON p.id = o.product_id
GROUP BY p.id, p.description
ORDER BY total_qty DESC
LIMIT 3;

-- 3) Суммарная выручка по дням за последние 14 дней
SELECT o.order_date, SUM(o.quantity * p.price) AS daily_revenue
FROM orders o
JOIN product p ON p.id = o.product_id
WHERE o.order_date >= CURRENT_DATE - INTERVAL '14 day'
GROUP BY o.order_date
ORDER BY o.order_date DESC;

-- 4) Все заказы в статусе 'NEW' или 'PAID' с покупателем и категорией товара
SELECT o.id, c.last_name, p.category, os.name AS status, o.quantity, o.order_date
FROM orders o
JOIN customer c ON c.id = o.customer_id
JOIN product  p ON p.id = o.product_id
JOIN order_status os ON os.id = o.status_id
WHERE os.name IN ('NEW', 'PAID')
ORDER BY o.order_date DESC, o.id DESC;

-- 5) Товары с нулевым остатком
SELECT id, description, quantity FROM product
WHERE quantity = 0;

-- ===== ИЗМЕНЕНИЕ (3+ UPDATE) =====

-- 6) Обновление цены товара (пример: скидка 10% на категорию 'Audio')
UPDATE product
SET price = ROUND(price * 0.9, 2)
WHERE category = 'Audio';

-- 7) Обновление количества на складе при «покупке» (пример: заказ id=1)
-- (обычно это делается транзакцией вместе с созданием заказа)
UPDATE product p
SET quantity = quantity - o.quantity
FROM orders o
WHERE o.id = 1
  AND o.product_id = p.id
  AND p.quantity >= o.quantity;

-- 8) Перевод всех заказов за вчерашний день в статус 'PAID'
UPDATE orders o
SET status_id = (SELECT id FROM order_status WHERE name = 'PAID')
WHERE o.order_date = CURRENT_DATE - INTERVAL '1 day';

-- ===== УДАЛЕНИЕ (2+ DELETE) =====

-- 9) Удаление клиентов без заказов
DELETE FROM customer c
WHERE NOT EXISTS (SELECT 1 FROM orders o WHERE o.customer_id = c.id);

-- 10) Удаление заказов в статусе 'CANCELED' старше 30 дней
DELETE FROM orders o
USING order_status os
WHERE o.status_id = os.id
  AND os.name = 'CANCELED'
  AND o.order_date < CURRENT_DATE - INTERVAL '30 day';
