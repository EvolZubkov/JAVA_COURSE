package com.example.dungeon.model;

/**
 * Монстр — враг игрока.
 * Добавлены:
 *  - поле attack (по умолчанию = level);
 *  - методы damage() и isAlive() для боя.
 */
public class Monster extends Entity {
    private int level;
    private int attack;

    public Monster(String name, int level, int hp) {
        super(name, hp);
        this.level = Math.max(1, level);
        this.attack = Math.max(1, level); // атака базово = уровень
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = Math.max(1, level);
    }

    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        this.attack = Math.max(0, attack);
    }

    /** Удобный метод для боя — уменьшает HP монстра. */
    public void damage(int amount) {
        if (amount <= 0) return;
        setHp(getHp() - amount);
    }

    /** Проверка, жив ли монстр. */
    public boolean isAlive() {
        return getHp() > 0;
    }

    @Override
    public String toString() {
        return getName() + " (ур. " + level + ", HP: " + getHp() + ", атака: " + attack + ")";
    }
}
