package ru.zubkov.streams;

import ru.zubkov.streams.repository.CarsRepository;
import ru.zubkov.streams.repository.CarsRepositoryImpl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;
import java.util.Locale;

public class ConsoleApp {

    public static void main(String[] args) throws Exception {
        Locale.setDefault(Locale.ROOT);

        // Читаем в системной кодировке (в PowerShell лучше выставить UTF-8)
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in, Charset.defaultCharset()));

        CarsRepository repo = new CarsRepositoryImpl();

        System.out.println("=== Ввод автомобилей ===");
        System.out.println("Формат строки: number|model|color|mileage|cost");
        System.out.println("Пример: a123me|Toyota|Black|108000|780000");
        System.out.println("Пустая строка — завершить ввод.\n");

        List<Car> cars = new ArrayList<>();
        while (true) {
            System.out.print("Запись: ");
            String line = br.readLine();
            if (line == null) break;
            line = line.trim();
            if (line.isEmpty()) break;

            Car car = parseCarLine(line);
            if (car == null) {
                System.out.println("  ❌ Неверный формат. Ожидается 5 полей, mileage/cost — числа. Повторите.");
                continue;
            }
            cars.add(car);
        }
        repo.addAll(cars);
        System.out.println("\nЗагружено записей: " + repo.findAll().size());
        System.out.println();

        while (true) {
            System.out.println("=== Меню ===");
            System.out.println("1) Номера авто по цвету ИЛИ с нулевым пробегом");
            System.out.println("2) Кол-во уникальных моделей в ценовом диапазоне [n; m]");
            System.out.println("3) Цвет автомобиля с минимальной стоимостью");
            System.out.println("4) Средняя стоимость по модели");
            System.out.println("0) Выход");
            System.out.print("Выбор: ");
            String choice = br.readLine();
            if (choice == null) break;
            choice = choice.trim();

            switch (choice) {
                case "1": {
                    System.out.print("Цвет (напр. Black): ");
                    String color = br.readLine();
                    var nums = repo.findNumbersByColorOrZeroMileage(color);
                    System.out.println("Номера: " + String.join("  ", nums));
                    System.out.println();
                    break;
                }
                case "2": {
                    System.out.print("n (мин. цена): ");
                    long n = parseLong(br.readLine());
                    System.out.print("m (макс. цена): ");
                    long m = parseLong(br.readLine());
                    long count = repo.countUniqueModelsInCostRange(n, m);
                    System.out.println("Уникальных моделей в диапазоне [" + Math.min(n,m) + ";" + Math.max(n,m) + "]: " + count);
                    System.out.println();
                    break;
                }
                case "3": {
                    String color = repo.colorOfMinCost().orElse("-");
                    System.out.println("Цвет авто с минимальной стоимостью: " + color);
                    System.out.println();
                    break;
                }
                case "4": {
                    System.out.print("Модель (напр. Toyota): ");
                    String model = br.readLine();
                    double avg = repo.averageCostByModel(model);
                    try (Formatter f = new Formatter(Locale.ROOT)) {
                        f.format("Средняя стоимость модели %s: %,.2f", model, avg);
                        System.out.println(f.toString());
                    }
                    System.out.println();
                    break;
                }
                case "0":
                    System.out.println("Выход. Пока!");
                    return;
                default:
                    System.out.println("Неизвестная команда.\n");
            }
        }
    }

    private static Car parseCarLine(String line) {
        try {
            String norm = Normalizer.normalize(line, Normalizer.Form.NFC);
            String[] p = norm.split("\\|");
            if (p.length != 5) return null;
            String number = p[0].trim();
            String model  = p[1].trim();
            String color  = p[2].trim();
            long mileage  = Long.parseLong(p[3].trim());
            long cost     = Long.parseLong(p[4].trim());
            return new Car(number, model, color, mileage, cost);
        } catch (Exception e) {
            return null;
        }
    }

    private static long parseLong(String s) {
        try { return Long.parseLong(s.trim()); }
        catch (Exception e) { return 0L; }
    }
}
