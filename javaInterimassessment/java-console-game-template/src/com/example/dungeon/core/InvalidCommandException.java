package com.example.dungeon.core;

/**
 * Исключение для ошибок пользовательских команд.
 * Бросается, когда:
 *  - команда неизвестна или написана с ошибкой;
 *  - не хватает аргументов (неверный синтаксис);
 *  - действие невозможно в текущем контексте (нет выхода, нет предмета, монстра и т.п.).
 *
 * Обрабатывается в Game.run(): игрок видит только понятный текст, без stack trace.
 */
public class InvalidCommandException extends Exception {
    private static final long serialVersionUID = 1L;

    public InvalidCommandException(String message) {
        super(message);
    }

    public InvalidCommandException(String message, Throwable cause) {
        super(message, cause);
    }
}
