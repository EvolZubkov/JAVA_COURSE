package ru.zubkov.shop;

import java.math.BigDecimal;
import java.util.Objects;

public class Product {
    private final String name;
    private final BigDecimal price; // базовая цена

    public Product(String name, BigDecimal price) {
        validateName(name);
        validatePrice(price);
        this.name = name.trim();
        this.price = price;
    }

    protected static void validateName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Название продукта не может быть null");
        }
        String n = name.trim();
        if (n.isEmpty()) {
            throw new IllegalArgumentException("Название продукта не может быть пустым");
        }
        if (n.length() < 3) {
            throw new IllegalArgumentException("Название должно быть длиной минимум 3 символа");
        }
        if (n.matches("\\d+")) {
            throw new IllegalArgumentException("Название не должно состоять только из цифр");
        }
    }

    protected static void validatePrice(BigDecimal price) {
        if (price == null) {
            throw new IllegalArgumentException("Цена не может быть null");
        }
        if (price.signum() <= 0) {
            throw new IllegalArgumentException("Цена должна быть положительной и больше 0");
        }
    }

    public String getName() {
        return name;
    }

    /** Возвращает текущую цену (для обычного продукта — базовую). */
    public BigDecimal getPrice() {
        return price;
    }

    public BigDecimal getBasePrice() {
        return price;
    }

    @Override
    public String toString() {
        return "Product{name='" + name + "', price=" + price + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product)) return false;
        Product product = (Product) o;
        return name.equals(product.name) && price.equals(product.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, price);
    }
}
