package ru.zubkov.collections;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.text.Normalizer;
import java.util.*;
import java.util.stream.Collectors;

public class Task3ConsoleMain {

    public static void mai1n(String[] args) throws Exception {
        // читаем в системной кодировке (чтобы не было проблем в Windows)
        var br = new BufferedReader(new InputStreamReader(System.in, Charset.defaultCharset()));

        System.out.println("Введите set1 (элементы через пробел/запятую/точку с запятой):");
        Set<String> set1 = readSet(br.readLine());

        System.out.println("Введите set2 (элементы через пробел/запятую/точку с запятой):");
        Set<String> set2 = readSet(br.readLine());

        System.out.println("set1 = " + set1);
        System.out.println("set2 = " + set2);

        System.out.println("intersection (set1 ∩ set2): " + PowerfulSet.intersection(set1, set2));
        System.out.println("union        (set1 ∪ set2): " + PowerfulSet.union(set1, set2));
        System.out.println("rel. compl.  (set1 \\ set2): " + PowerfulSet.relativeComplement(set1, set2));
    }

    private static Set<String> readSet(String line) {
        if (line == null) return Collections.emptySet();
        // Нормализуем Unicode и режем по пробелам/запятым/точкам с запятой
        String n = Normalizer.normalize(line, Normalizer.Form.NFC);
        List<String> items = Arrays.stream(n.trim().split("[,;\\s]+"))
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
        // HashSet — порядок не гарантируется (это нормально для множеств)
        return new HashSet<>(items);
    }
}
