package com.example.dungeon.model;

/**
 * Класс зелья (лечит игрока).
 * Демонстрация полиморфизма: каждый тип Item реализует свой apply().
 */
public class Potion extends Item {
    private final int heal;

    public Potion(String name, int heal) {
        super(name);
        this.heal = heal;
    }

    @Override
    public void apply(GameState ctx) {
        Player p = ctx.getPlayer();
        int before = p.getHp();

        // Лечение с защитой от превышения максимума (если поле maxHp существует)
        int newHp = p.getHp() + heal;
        try {
            // Если у Player есть maxHp — корректируем
            var method = p.getClass().getMethod("getMaxHp");
            int maxHp = (int) method.invoke(p);
            newHp = Math.min(newHp, maxHp);
        } catch (Exception ignored) { /* если нет maxHp — просто лечим */ }

        p.setHp(newHp);
        System.out.println("Выпито зелье: +" + (p.getHp() - before) + " HP. Текущее HP: " + p.getHp());
        p.getInventory().remove(this);
    }
}
