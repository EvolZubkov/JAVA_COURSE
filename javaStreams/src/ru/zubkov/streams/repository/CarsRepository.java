package ru.zubkov.streams.repository;

import ru.zubkov.streams.Car;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

public interface CarsRepository {
    List<Car> findAll();
    void addAll(List<Car> cars);

    /** 1) Номера авто по цвету colorToFind ИЛИ нулевому пробегу */
    List<String> findNumbersByColorOrZeroMileage(String colorToFind);

    /** 2) Кол-во уникальных моделей в ценовом диапазоне [n; m] (в рублях) */
    long countUniqueModelsInCostRange(long n, long m);

    /** 3) Цвет автомобиля с минимальной стоимостью */
    Optional<String> colorOfMinCost();

    /** 4) Средняя стоимость искомой модели (0.0, если нет таких) */
    double averageCostByModel(String modelToFind);

    // IO:
    List<Car> loadFromFile(Path path) throws IOException;
    void saveReport(Path path, List<String> lines) throws IOException;
}
