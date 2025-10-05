package com.example.dungeon.model;

/**
 * Абстрактный базовый класс для всех предметов в игре.
 * Каждый предмет реализует собственное поведение через apply(GameState).
 */
public abstract class Item {
    private final String name;

    protected Item(String name) {
        this.name = name;
    }

    /** Имя предмета (используется при take/use/inventory). */
    public String getName() {
        return name;
    }

    /**
     * Полиморфное действие предмета.
     * Например:
     *  - Potion.apply: лечит игрока;
     *  - Weapon.apply: повышает атаку;
     *  - Key.apply: сообщает, что дверь закрыта.
     */
    public abstract void apply(GameState ctx);

    /** Тип предмета для группировки в команде inventory. */
    public String type() {
        return getClass().getSimpleName();
    }

    /** Упрощает вывод предметов в консоль (например, при отладке или inventory). */
    @Override
    public String toString() {
        return name;
    }
}
