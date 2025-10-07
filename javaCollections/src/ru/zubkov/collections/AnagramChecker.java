package ru.zubkov.collections;

import java.util.HashMap;
import java.util.Map;

public final class AnagramChecker {
    private AnagramChecker() {}

    /**
     * Возвращает true, если s и t — анаграммы.
     * Игнорируются пробелы и регистр; все прочие символы учитываются как есть.
     */
    public static boolean isValidAnagram(String s, String t) {
        if (s == null || t == null) return false;

        String a = normalize(s);
        String b = normalize(t);
        if (a.length() != b.length()) return false;

        Map<Integer, Integer> freq = new HashMap<>();
        a.codePoints().forEach(cp -> freq.merge(cp, 1, Integer::sum));

        for (int cp : b.codePoints().toArray()) {
            Integer cnt = freq.get(cp);
            if (cnt == null || cnt == 0) return false;
            if (cnt == 1) freq.remove(cp);
            else freq.put(cp, cnt - 1);
        }
        return freq.isEmpty();
    }

    private static String normalize(String s) {
        // Убираем все пробельные символы (включая табы/множественные пробелы) и приводим к нижнему регистру.
        return s.replaceAll("\\s+", "").toLowerCase();
    }
}
