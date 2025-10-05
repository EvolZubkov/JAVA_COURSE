package com.example.dungeon.core;

import com.example.dungeon.model.GameState;
import java.util.List;

/**
 * Функциональный интерфейс для всех команд игры.
 * Позволяет использовать лямбды и бросать InvalidCommandException.
 */
@FunctionalInterface
public interface Command {
    void execute(GameState ctx, List<String> args) throws InvalidCommandException;
}
