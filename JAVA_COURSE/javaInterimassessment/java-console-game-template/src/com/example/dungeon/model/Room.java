package com.example.dungeon.model;

import java.util.*;

/**
 * Комната: имя, описание, выходы, предметы, монстр.
 * Улучшения:
 *  - стабильный порядок вывода выходов и предметов;
 *  - нормализация направлений (lower-case) при линковке/чтении;
 *  - describe() не показывает мёртвого монстра;
 *  - удобный sugar-метод link(dir, room).
 */
public class Room {
    private final String name;
    private final String description;
    private final Map<String, Room> neighbors = new HashMap<>();
    private final List<Item> items = new ArrayList<>();
    private Monster monster;

    public Room(String name, String description) {
        this.name = Objects.requireNonNull(name);
        this.description = Objects.requireNonNull(description);
    }

    public String getName() { return name; }

    /** Может пригодиться, например, для about/look. */
    public String getDescription() { return description; }

    public Map<String, Room> getNeighbors() { return neighbors; }

    public List<Item> getItems() { return items; }

    public Monster getMonster() { return monster; }

    public void setMonster(Monster m) { this.monster = m; }

    /** Утилита для сборки мира: связывает комнаты по направлению. */
    public void link(String dir, Room target) {
        neighbors.put(dir.toLowerCase(Locale.ROOT), target);
    }

    /** Описание комнаты в формате, ожидаемом командами. */
    public String describe() {
        StringBuilder sb = new StringBuilder();
        sb.append(name).append(": ").append(description);

        if (!items.isEmpty()) {
            // Стабильный и читабельный вывод предметов
            List<String> names = new ArrayList<>();
            for (Item i : items) names.add(i.getName());
            Collections.sort(names, String.CASE_INSENSITIVE_ORDER);
            sb.append(System.lineSeparator())
              .append("Предметы: ")
              .append(String.join(", ", names));
        }

        if (monster != null && monster.getHp() > 0) {
            sb.append(System.lineSeparator())
              .append("В комнате монстр: ")
              .append(monster.getName())
              .append(" (ур. ").append(monster.getLevel()).append(")");
        }

        // Стабильный порядок направлений
        if (!neighbors.isEmpty()) {
            List<String> dirs = new ArrayList<>(neighbors.keySet());
            dirs.replaceAll(d -> d.toLowerCase(Locale.ROOT));
            Collections.sort(dirs);
            sb.append(System.lineSeparator())
              .append("Выходы: ")
              .append(String.join(", ", dirs));
        }
        return sb.toString();
    }
}
