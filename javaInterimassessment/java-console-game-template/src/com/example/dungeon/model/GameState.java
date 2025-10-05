package com.example.dungeon.model;

/**
 * Контейнер состояния игры:
 *  - текущий игрок;
 *  - активная комната;
 *  - счёт.
 *
 * Добавлены проверки и защита от отрицательных значений.
 */
public class GameState {
    private Player player;
    private Room current;
    private int score;

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player p) {
        if (p == null) {
            throw new IllegalArgumentException("Игрок не может быть null");
        }
        this.player = p;
    }

    public Room getCurrent() {
        return current;
    }

    public void setCurrent(Room r) {
        if (r == null) {
            throw new IllegalArgumentException("Комната не может быть null");
        }
        this.current = r;
    }

    public int getScore() {
        return score;
    }

    /** Безопасное изменение счёта (не уходит в минус). */
    public void addScore(int delta) {
        long tmp = (long) this.score + delta;
        if (tmp < 0) tmp = 0;
        if (tmp > Integer.MAX_VALUE) tmp = Integer.MAX_VALUE;
        this.score = (int) tmp;
    }
}
