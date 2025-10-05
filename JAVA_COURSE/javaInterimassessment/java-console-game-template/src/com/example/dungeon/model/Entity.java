package com.example.dungeon.model;

/**
 * Базовый класс для Player и Monster.
 * Добавлена базовая валидация и метод isAlive().
 */
public abstract class Entity {
    private String name;
    private int hp;

    public Entity(String name, int hp) {
        this.name = (name == null || name.isBlank()) ? "Unknown" : name;
        this.hp = Math.max(0, hp);
    }

    public String getName() { return name; }

    public void setName(String name) {
        if (name != null && !name.isBlank()) {
            this.name = name;
        }
    }

    public int getHp() { return hp; }

    /** Не даём HP уходить в минус. */
    public void setHp(int hp) {
        this.hp = Math.max(0, hp);
    }

    /** Проверка, жив ли объект. */
    public boolean isAlive() {
        return hp > 0;
    }

    @Override
    public String toString() {
        return name + " (HP: " + hp + ")";
    }
}
