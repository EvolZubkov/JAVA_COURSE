package ru.zubkov.streams;

import ru.zubkov.streams.repository.CarsRepository;
import ru.zubkov.streams.repository.CarsRepositoryImpl;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;
import java.util.Locale;

public class Main {
    public static void main(String[] args) throws Exception {
        Locale.setDefault(Locale.ROOT);
        CarsRepository repo = new CarsRepositoryImpl();

        // 1) читаем входные данные из файла io/input.txt
        Path in = Path.of("io", "input.txt");
        List<Car> cars = repo.loadFromFile(in);
        repo.addAll(cars);

        // 2) печать таблицы в консоль
        System.out.println("Автомобили в базе:");
        System.out.printf("  %-8s %-10s %-7s %7s %12s%n", "Number", "Model", "Color", "Mileage", "Cost");
        repo.findAll().forEach(c -> System.out.println("  " + c));

        // переменные из задания
        String colorToFind = "Black";
        long n = 700_000L, m = 800_000L;
        String modelToFind = "Toyota";
        String modelAbsent = "Volvo";

        // 3) Stream-вычисления
        List<String> numsByColorOrZero = repo.findNumbersByColorOrZeroMileage(colorToFind);
        long uniqueModelsInRange = repo.countUniqueModelsInCostRange(n, m);
        String minCostColor = repo.colorOfMinCost().orElse("-");
        double avgToyota = repo.averageCostByModel(modelToFind);
        double avgVolvo  = repo.averageCostByModel(modelAbsent);

        // 4) вывод + запись в файл io/output.txt
        List<String> outLines = new ArrayList<>();

        String numsLine = "Номера автомобилей по цвету или пробегу: " + String.join("  ", numsByColorOrZero);
        System.out.println(numsLine); outLines.add(numsLine);

        String uniqLine = "Уникальные автомобили: " + uniqueModelsInRange + " шт.";
        System.out.println(uniqLine); outLines.add(uniqLine);

        String minColorLine = "Цвет автомобиля с минимальной стоимостью: " + minCostColor;
        System.out.println(minColorLine); outLines.add(minColorLine);

        try (Formatter f = new Formatter(Locale.ROOT)) {
            f.format("Средняя стоимость модели %s: %,.2f", modelToFind, avgToyota);
            String s1 = f.toString(); System.out.println(s1); outLines.add(s1);
        }
        try (Formatter f = new Formatter(Locale.ROOT)) {
            f.format("Средняя стоимость модели %s: %,.2f", modelAbsent, avgVolvo);
            String s2 = f.toString(); System.out.println(s2); outLines.add(s2);
        }

        Path out = Path.of ("io", "output.txt");
        repo.saveReport(out, outLines);
        System.out.println("\nОтчёт сохранён: " + out.toAbsolutePath());
    }
}
