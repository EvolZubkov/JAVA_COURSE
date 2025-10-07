package ru.zubkov.shop;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Скидочный продукт: цена уменьшается на процент скидки до даты discountUntil (включительно).
 * После discountUntil скидка перестаёт действовать (возвращается базовая цена).
 */
public class DiscountProduct extends Product {

    private final int discountPercent;      // 1..99
    private final LocalDate discountUntil;  // включительно

    public DiscountProduct(String name, BigDecimal basePrice, int discountPercent, LocalDate discountUntil) {
        super(name, basePrice);
        validateDiscountPercent(discountPercent);
        validateDiscountUntil(discountUntil);
        this.discountPercent = discountPercent;
        this.discountUntil = discountUntil;
    }

    private static void validateDiscountPercent(int percent) {
        if (percent < 1 || percent > 99) {
            throw new IllegalArgumentException("Скидка должна быть в диапазоне 1..99%");
        }
    }

    private static void validateDiscountUntil(LocalDate until) {
        if (until == null) {
            throw new IllegalArgumentException("Дата окончания скидки не может быть null");
        }
        // Разрешим сегодняшнюю и будущие даты; прошедшее — бессмысленно при создании
        if (until.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Дата окончания скидки не может быть в прошлом");
        }
    }

    public int getDiscountPercent() {
        return discountPercent;
    }

    public LocalDate getDiscountUntil() {
        return discountUntil;
    }

    public boolean isDiscountActive(LocalDate today) {
        return !today.isAfter(discountUntil);
    }

    @Override
    public BigDecimal getPrice() {
        if (isDiscountActive(LocalDate.now())) {
            BigDecimal hundred = BigDecimal.valueOf(100);
            BigDecimal multiplier = hundred.subtract(BigDecimal.valueOf(discountPercent))
                                           .divide(hundred, 4, RoundingMode.HALF_UP);
            BigDecimal discounted = getBasePrice().multiply(multiplier);
            // Дополнительно страхуемся от нулевой/отрицательной — по условию цена продукта не может быть ≤ 0
            if (discounted.signum() <= 0) {
                throw new IllegalStateException("Итоговая цена со скидкой недопустима (<= 0)");
            }
            return discounted.setScale(2, RoundingMode.HALF_UP);
        }
        return super.getPrice();
    }

    @Override
    public String toString() {
        return "DiscountProduct{name='" + getName() + "', basePrice=" + getBasePrice()
                + ", discountPercent=" + discountPercent
                + ", discountUntil=" + discountUntil
                + ", currentPrice=" + getPrice() + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DiscountProduct)) return false;
        if (!super.equals(o)) return false;
        DiscountProduct that = (DiscountProduct) o;
        return discountPercent == that.discountPercent && discountUntil.equals(that.discountUntil);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), discountPercent, discountUntil);
    }
}
