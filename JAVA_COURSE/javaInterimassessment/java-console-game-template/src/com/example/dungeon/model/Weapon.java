package com.example.dungeon.model;

/**
 * Оружие — предмет, который повышает атаку игрока.
 * Полиморфизм: Game вызывает item.apply(ctx),
 * но именно Weapon определяет, что делать — увеличить урон.
 */
public class Weapon extends Item {
    private final int bonus;

    public Weapon(String name, int bonus) {
        super(name);
        // защита от некорректного бонуса
        this.bonus = Math.max(0, bonus);
    }

    @Override
    public void apply(GameState ctx) {
        Player p = ctx.getPlayer();
        int before = p.getAttack();
        p.setAttack(before + bonus);

        System.out.printf(
            "Вы экипировали оружие: %s. Атака увеличена с %d до %d (+%d)%n",
            getName(), before, p.getAttack(), bonus
        );

        // После использования убираем предмет из инвентаря
        p.getInventory().remove(this);
    }
}
