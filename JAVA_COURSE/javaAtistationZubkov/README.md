✅ Итог проекта Orders App (PostgreSQL + JDBC + Maven)
📦 Структура проекта
orders-app/
 ├─ pom.xml                      ← Maven-конфигурация (Flyway, JDBC, DBCP2)
 ├─ docker-compose.yml            ← PostgreSQL 16 в контейнере
 ├─ src/main/java/com/example/app/App.java  ← Основная логика и CRUD-демо
 ├─ src/main/resources/
 │   ├─ application.properties    ← параметры подключения
 │   ├─ db/migration/
 │   │    ├─ V1__schema.sql       ← создание таблиц с ограничениями
 │   │    └─ V2__seed.sql         ← тестовые данные (10+ строк)
 │   └─ sql/test-queries.sql      ← 10 демонстрационных SQL-запросов

🧱 База данных

Схема: customer, product, orders, order_status

Ограничения: NOT NULL, CHECK, FOREIGN KEY, индексы, комментарии

Миграции через Flyway (V1, V2)

Сидинг: ≥10 строк в каждую таблицу

💡 Функционал приложения (App.java)

Подключение к PostgreSQL через JDBC + DBCP2

Автоматический запуск миграций Flyway

CRUD-операции:

Вставка нового товара и покупателя (PreparedStatement)

Создание заказа с проверкой остатка и уменьшением количества

Чтение последних 5 заказов (JOIN с 3 таблицами)

Обновление цены и количества товара

Удаление тестовых записей

Работа в транзакции с commit() / rollback()

Красивый вывод в консоль

🧰 Инфраструктура

PostgreSQL 16 в Docker (docker compose up -d)

Flyway 10.22 с модулем flyway-database-postgresql

Java 17, Maven, без Spring

🧾 Проверка SQL-запросов

Файл test-queries.sql (10 запросов: 5 SELECT, 3 UPDATE, 2 DELETE)

Можно выполнять в psql, Docker, IntelliJ IDEA или DBeaver

📸 Что приложить к отчёту

Скриншоты:

Поднятая БД / подключение в IDE

Выполнение миграций Flyway

ER-диаграмма (можно в Mermaid, как я показал выше)

Запуск App.java и вывод CRUD-операций

Результаты test-queries.sql

🎯 Итог: проект полностью соответствует требованиям итоговой аттестации:

✅ PostgreSQL установлен и работает

✅ Схема и миграции созданы

✅ CRUD-операции реализованы

✅ SQL-запросы подготовлены

✅ Maven-проект на Java 17 оформлен корректно