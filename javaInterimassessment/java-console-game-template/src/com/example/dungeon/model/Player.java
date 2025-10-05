package com.example.dungeon.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Игрок: здоровье, атака и инвентарь.
 * Добавлены:
 *  - maxHp: верхняя граница здоровья;
 *  - защита от отрицательной атаки;
 *  - удобные методы heal()/damage() для предметов и боя.
 */
public class Player extends Entity {
    private int attack;
    private final int maxHp;
    private final List<Item> inventory = new ArrayList<>();

    public Player(String name, int hp, int attack) {
        super(name, hp);
        this.attack = Math.max(0, attack);
        this.maxHp = hp; // стартовое HP считаем максимальным
    }

    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        this.attack = Math.max(0, attack);
    }

    /** Максимальное здоровье (для защиты от "перелечения"). */
    public int getMaxHp() {
        return maxHp;
    }

    public List<Item> getInventory() {
        return inventory;
    }

    /** Аккуратное лечение: не выше maxHp. */
    public void heal(int amount) {
        if (amount <= 0) return;
        setHp(Math.min(maxHp, getHp() + amount));
    }

    /** Получение урона: не даём HP уйти ниже нуля (Entity.setHp уже страхует, но оставляем явную семантику). */
    public void damage(int amount) {
        if (amount <= 0) return;
        setHp(getHp() - amount);
    }

    @Override
    public String toString() {
        return getName() + " (HP: " + getHp() + "/" + maxHp + ", атака: " + attack + ")";
    }
}
