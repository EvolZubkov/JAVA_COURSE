package ru.zubkov.collections;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public final class CollectionsUtils {
    private CollectionsUtils() {}

    /** Возвращает уникальные элементы списка, сохраняя порядок первого появления. */
    public static <T> Set<T> uniqueElements(List<T> list) {
        if (list == null) {
            throw new IllegalArgumentException("list == null");
        }
        return new LinkedHashSet<>(list);
    }
}
