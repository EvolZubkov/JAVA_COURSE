package ru.zubkov.collections;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

public class Task1ConsoleMain {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in, java.nio.charset.StandardCharsets.UTF_8));

        System.out.println("Введите элементы через пробел/запятую/точку с запятой:");
        String line = br.readLine();

        if (line == null || line.trim().isEmpty()) {
            System.out.println("Пустой ввод. Нечего обрабатывать.");
            return;
        }

        // Разделяем по пробелам, запятым и точкам с запятой (любой их микс)
        List<String> items = Arrays.stream(line.trim().split("[,;\\s]+"))
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());

        Set<String> unique = CollectionsUtils.uniqueElements(items);

        System.out.println("Исходные элементы: " + items);
        System.out.println("Уникальные (порядок сохранён): " + unique);
        System.out.println("Количество уникальных: " + unique.size());
    }
}
