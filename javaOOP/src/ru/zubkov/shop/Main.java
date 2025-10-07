package ru.zubkov.shop;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        Product bread = new Product("Хлеб", BigDecimal.valueOf(55));
        DiscountProduct milk = new DiscountProduct("Молоко",
                BigDecimal.valueOf(120), 20, LocalDate.now().plusDays(5));

        System.out.println(bread + " -> current price: " + bread.getPrice());
        System.out.println(milk + " -> current price: " + milk.getPrice());
    }
}
