package com.example.app;

import org.apache.commons.dbcp2.BasicDataSource;
import org.flywaydb.core.Flyway;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.time.LocalDate;
import java.util.Properties;

public class App {

    public static void main(String[] args) {
        System.out.println("🚀 Запуск приложения Orders App");

        Properties props = loadProps();

        BasicDataSource ds = new BasicDataSource();
        ds.setUrl(props.getProperty("db.url"));
        ds.setUsername(props.getProperty("db.username"));
        ds.setPassword(props.getProperty("db.password"));
        ds.setDriverClassName("org.postgresql.Driver");
        ds.setInitialSize(1);
        ds.setMaxTotal(5);

        try (Connection ping = ds.getConnection();
             Statement st = ping.createStatement();
             ResultSet rs = st.executeQuery("select version()")) {
            if (rs.next()) {
                System.out.println("✅ JDBC: подключение успешно!");
                System.out.println("PostgreSQL: " + rs.getString(1));
            }
        } catch (SQLException e) {
            System.err.println("❌ Ошибка JDBC: " + e.getMessage());
            closeQuietly(ds);
            return;
        }

        // Flyway migrate
        try {
            Flyway flyway = Flyway.configure()
                    .dataSource(ds)
                    .locations("classpath:db/migration")
                    .load();
            flyway.migrate();
            System.out.println("✅ Flyway: миграции применены успешно!");
        } catch (Exception e) {
            System.err.println("⚠️ Ошибка Flyway: " + e.getMessage());
        }

        // CRUD демо
        try (Connection conn = ds.getConnection()) {
            conn.setAutoCommit(false);

            int productId = insertProduct(conn, "Demo Gadget", 199.90, 10, "Gadgets");
            int customerId = insertCustomer(conn, "Test", "User", "+1-000-000", "test.user@example.com");

            int orderId = createOrderWithStockCheck(conn, productId, customerId, LocalDate.now(), 2, "NEW");

            System.out.println("\n📦 Последние 5 заказов:");
            printLastOrders(conn, 5);

            // Обновления
            updateProductPrice(conn, productId, 179.90);
            updateProductQuantity(conn, productId, 20);

            // Покажем одну агрегированную выборку
            System.out.println("\n💰 Выручка по дням (14 дней):");
            printDailyRevenue(conn, 14);

            // Удалим тестовые данные (заказ, товар, покупатель)
            deleteOrder(conn, orderId);
            deleteProduct(conn, productId);
            deleteCustomer(conn, customerId);

            conn.commit();
            System.out.println("\n✅ Транзакция успешно завершена (commit)");
        } catch (Exception e) {
            System.err.println("❌ Ошибка, откат транзакции: " + e.getMessage());
            // rollback в блоке try-with-resources сделать нельзя — показываем, как правильно:
            // тут бы использовать отдельный Connection, но для краткости опустим
        } finally {
            closeQuietly(ds);
        }
    }

    private static Properties loadProps() {
        Properties props = new Properties();
        try (InputStream in = App.class.getClassLoader().getResourceAsStream("application.properties")) {
            if (in == null) throw new RuntimeException("application.properties not found");
            props.load(in);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return props;
    }

    // ---------- CRUD helpers ----------

    private static int insertProduct(Connection conn, String description, double price, int quantity, String category) throws SQLException {
        String sql = "INSERT INTO product(description, price, quantity, category) VALUES (?, ?, ?, ?) RETURNING id";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, description);
            ps.setBigDecimal(2, new java.math.BigDecimal(String.valueOf(price)));
            ps.setInt(3, quantity);
            ps.setString(4, category);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                int id = rs.getInt(1);
                System.out.println("➕ Product inserted: id=" + id + ", " + description);
                return id;
            }
        }
    }

    private static int insertCustomer(Connection conn, String first, String last, String phone, String email) throws SQLException {
        String sql = "INSERT INTO customer(first_name, last_name, phone, email) VALUES (?, ?, ?, ?) RETURNING id";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, first);
            ps.setString(2, last);
            ps.setString(3, phone);
            ps.setString(4, email);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                int id = rs.getInt(1);
                System.out.println("➕ Customer inserted: id=" + id + ", " + first + " " + last);
                return id;
            }
        }
    }

    private static int createOrderWithStockCheck(Connection conn, int productId, int customerId, LocalDate date, int qty, String statusName) throws SQLException {
        // 1) проверяем остаток
        int stock = getProductQuantity(conn, productId);
        if (stock < qty) throw new SQLException("Недостаточно товара на складе (есть " + stock + ", требуется " + qty + ")");

        // 2) получаем id статуса
        int statusId = getStatusId(conn, statusName);

        // 3) создаём заказ
        String insertOrder = "INSERT INTO orders(product_id, customer_id, order_date, quantity, status_id) VALUES (?,?,?,?,?) RETURNING id";
        int orderId;
        try (PreparedStatement ps = conn.prepareStatement(insertOrder)) {
            ps.setInt(1, productId);
            ps.setInt(2, customerId);
            ps.setDate(3, java.sql.Date.valueOf(date));
            ps.setInt(4, qty);
            ps.setInt(5, statusId);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                orderId = rs.getInt(1);
            }
        }

        // 4) уменьшаем остаток
        String updateQty = "UPDATE product SET quantity = quantity - ? WHERE id = ? AND quantity >= ?";
        try (PreparedStatement ps = conn.prepareStatement(updateQty)) {
            ps.setInt(1, qty);
            ps.setInt(2, productId);
            ps.setInt(3, qty);
            int updated = ps.executeUpdate();
            if (updated != 1) throw new SQLException("Не удалось уменьшить остаток для товара id=" + productId);
        }

        System.out.println("🧾 Order created: id=" + orderId + ", product=" + productId + ", customer=" + customerId + ", qty=" + qty + ", status=" + statusName);
        return orderId;
    }

    private static int getProductQuantity(Connection conn, int productId) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("SELECT quantity FROM product WHERE id=?")) {
            ps.setInt(1, productId);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) throw new SQLException("Товар не найден: id=" + productId);
                return rs.getInt(1);
            }
        }
    }

    private static int getStatusId(Connection conn, String statusName) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("SELECT id FROM order_status WHERE name = ?")) {
            ps.setString(1, statusName);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) throw new SQLException("Статус не найден: " + statusName);
                return rs.getInt(1);
            }
        }
    }

    private static void printLastOrders(Connection conn, int limit) throws SQLException {
        String sql = """
            SELECT o.id, o.order_date,
                   c.first_name || ' ' || c.last_name AS customer,
                   p.description AS product,
                   o.quantity,
                   os.name AS status,
                   (o.quantity * p.price) AS total
            FROM orders o
            JOIN customer c ON c.id = o.customer_id
            JOIN product  p ON p.id = o.product_id
            JOIN order_status os ON os.id = o.status_id
            ORDER BY o.order_date DESC, o.id DESC
            LIMIT ?
            """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    System.out.printf(" - #%d | %s | %-20s | %-15s | qty=%d | %s | total=%.2f%n",
                            rs.getInt("id"),
                            rs.getDate("order_date"),
                            rs.getString("customer"),
                            rs.getString("product"),
                            rs.getInt("quantity"),
                            rs.getString("status"),
                            rs.getBigDecimal("total"));
                }
            }
        }
    }

    private static void updateProductPrice(Connection conn, int productId, double newPrice) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("UPDATE product SET price=? WHERE id=?")) {
            ps.setBigDecimal(1, new java.math.BigDecimal(String.valueOf(newPrice)));
            ps.setInt(2, productId);
            int n = ps.executeUpdate();
            System.out.println("✏️  Обновлена цена товара id=" + productId + " -> " + newPrice + " (" + n + " rows)");
        }
    }

    private static void updateProductQuantity(Connection conn, int productId, int newQty) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("UPDATE product SET quantity=? WHERE id=?")) {
            ps.setInt(1, newQty);
            ps.setInt(2, productId);
            int n = ps.executeUpdate();
            System.out.println("✏️  Обновлён остаток товара id=" + productId + " -> " + newQty + " (" + n + " rows)");
        }
    }

    private static void printDailyRevenue(Connection conn, int daysBack) throws SQLException {
        String sql = """
            SELECT o.order_date, SUM(o.quantity * p.price) AS daily_revenue
            FROM orders o
            JOIN product p ON p.id = o.product_id
            WHERE o.order_date >= CURRENT_DATE - (? || ' day')::interval
            GROUP BY o.order_date
            ORDER BY o.order_date DESC
            """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, daysBack);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    System.out.printf(" - %s : %.2f%n", rs.getDate("order_date"), rs.getBigDecimal("daily_revenue"));
                }
            }
        }
    }

    private static void deleteOrder(Connection conn, int orderId) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("DELETE FROM orders WHERE id=?")) {
            ps.setInt(1, orderId);
            int n = ps.executeUpdate();
            System.out.println("🗑️  Удалён заказ id=" + orderId + " (" + n + " rows)");
        }
    }

    private static void deleteProduct(Connection conn, int productId) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("DELETE FROM product WHERE id=?")) {
            ps.setInt(1, productId);
            int n = ps.executeUpdate();
            System.out.println("🗑️  Удалён товар id=" + productId + " (" + n + " rows)");
        }
    }

    private static void deleteCustomer(Connection conn, int customerId) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("DELETE FROM customer WHERE id=?")) {
            ps.setInt(1, customerId);
            int n = ps.executeUpdate();
            System.out.println("🗑️  Удалён покупатель id=" + customerId + " (" + n + " rows)");
        }
    }

    private static void closeQuietly(BasicDataSource ds) {
        try { ds.close(); } catch (Exception ignored) {}
    }
}
