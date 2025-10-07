package ru.zubkov.streams;

import java.util.Objects;

public class Car {
    private String number;   // Номер автомобиля
    private String model;    // Модель
    private String color;    // Цвет
    private long mileage;    // Пробег
    private long cost;       // Стоимость

    public Car() { }

    public Car(String number, String model, String color, long mileage, long cost) {
        this.number = number;
        this.model = model;
        this.color = color;
        this.mileage = mileage;
        this.cost = cost;
    }

    public String getNumber() { return number; }
    public void setNumber(String number) { this.number = number; }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public long getMileage() { return mileage; }
    public void setMileage(long mileage) { this.mileage = mileage; }

    public long getCost() { return cost; }
    public void setCost(long cost) { this.cost = cost; }

    @Override
    public String toString() {
        return String.format("%-8s %-10s %-7s %7d %12d", number, model, color, mileage, cost);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Car)) return false;
        Car car = (Car) o;
        return mileage == car.mileage && cost == car.cost &&
               Objects.equals(number, car.number) &&
               Objects.equals(model, car.model) &&
               Objects.equals(color, car.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(number, model, color, mileage, cost);
    }
}
