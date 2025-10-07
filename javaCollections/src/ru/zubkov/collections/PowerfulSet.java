package ru.zubkov.collections;

import java.util.HashSet;
import java.util.Set;

public class PowerfulSet {

    /** Пересечение set1 ∩ set2. */
    public static <T> Set<T> intersection(Set<T> set1, Set<T> set2) {
        check(set1, set2);
        Set<T> res = new HashSet<>(set1);
        res.retainAll(set2);
        return res;
    }

    /** Объединение set1 ∪ set2. */
    public static <T> Set<T> union(Set<T> set1, Set<T> set2) {
        check(set1, set2);
        Set<T> res = new HashSet<>(set1);
        res.addAll(set2);
        return res;
    }

    /** Относительное дополнение set1 \ set2 (элементы set1, которых нет в set2). */
    public static <T> Set<T> relativeComplement(Set<T> set1, Set<T> set2) {
        check(set1, set2);
        Set<T> res = new HashSet<>(set1);
        res.removeAll(set2);
        return res;
    }

    private static void check(Object a, Object b) {
        if (a == null || b == null) {
            throw new IllegalArgumentException("sets must not be null");
        }
    }
}
