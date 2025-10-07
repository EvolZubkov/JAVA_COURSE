package ru.zubkov.streams.repository;

import ru.zubkov.streams.Car;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.Normalizer;
import java.util.*;
import java.util.stream.Collectors;

public class CarsRepositoryImpl implements CarsRepository {
    private final List<Car> storage = new ArrayList<>();

    @Override
    public List<Car> findAll() {
        return Collections.unmodifiableList(storage);
    }

    @Override
    public void addAll(List<Car> cars) {
        storage.addAll(cars);
    }

    @Override
    public List<String> findNumbersByColorOrZeroMileage(String colorToFind) {
        String color = normalize(colorToFind);
        return storage.stream()
                .filter(c -> c.getMileage() == 0 || normalize(c.getColor()).equals(color))
                .map(Car::getNumber)
                .collect(Collectors.toList());
    }

    @Override
    public long countUniqueModelsInCostRange(long n, long m) {
        long min = Math.min(n, m), max = Math.max(n, m);
        return storage.stream()
                .filter(c -> c.getCost() >= min && c.getCost() <= max)
                .map(Car::getModel)
                .map(this::normalize)
                .distinct()
                .count();
    }

    @Override
    public Optional<String> colorOfMinCost() {
        return storage.stream()
                .min(Comparator.comparingLong(Car::getCost))
                .map(Car::getColor);
    }

    @Override
    public double averageCostByModel(String modelToFind) {
        String model = normalize(modelToFind);
        return storage.stream()
                .filter(c -> normalize(c.getModel()).equals(model))
                .mapToLong(Car::getCost)
                .average()
                .orElse(0.0);
    }

    // ===== IO =====
    @Override
    public List<Car> loadFromFile(Path path) throws IOException {
        List<Car> result = new ArrayList<>();
        try (BufferedReader br = Files.newBufferedReader(path, Charset.forName("UTF-8"))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                String[] parts = line.split("\\|");
                if (parts.length != 5) continue;
                Car car = new Car(
                        parts[0].trim(),
                        parts[1].trim(),
                        parts[2].trim(),
                        Long.parseLong(parts[3].trim()),
                        Long.parseLong(parts[4].trim())
                );
                result.add(car);
            }
        }
        return result;
    }

    @Override
    public void saveReport(Path path, List<String> lines) throws IOException {
        Files.createDirectories(path.getParent());
        try (BufferedWriter bw = Files.newBufferedWriter(path, Charset.forName("UTF-8"))) {
            for (String s : lines) {
                bw.write(s);
                bw.newLine();
            }
        }
    }

    private String normalize(String s) {
        if (s == null) return "";
        return Normalizer.normalize(s, Normalizer.Form.NFC)
                .trim()
                .toLowerCase(Locale.ROOT);
    }
}
